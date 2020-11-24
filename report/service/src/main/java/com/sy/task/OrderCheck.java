package com.sy.task;


import com.sy.dao.*;
import com.sy.entity.*;
import com.sy.service.IProcessDetailService;
import com.sy.service.IProcessTypeService;
import com.sy.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Component
public class OrderCheck {

    @Autowired
    private TableInfoMapper infoMapper;

    @Autowired
    private TableTypeMapper tableTypeMapper;

    @Autowired
    private ProcessDetailMapper detailMapper;

    @Autowired
    private ProcessConditionMapper conditionMapper;

    @Autowired
    private ProcessRunMapper runMapper;

    @Autowired
    private TableDataMapper dataMapper;

    @Scheduled(cron = "0 */5 * * * ?") // 5分钟
    @Transactional
    public void handleData(){

        TableInfo info = infoMapper.getInfo();

        String dataTableName = info.getDataTableName();

        String typeTableName = info.getTypeTableName();

        String typeTableFlied = info.getTypeTableFiled();

        //获取数据表单中正常状态以及未打上标记的表单名称以及表单类型

        List<DataType> uncheckedOrder = dataMapper.getUncheckedOrder(dataTableName);

        List<ProcessType> types = tableTypeMapper.getType(typeTableFlied,typeTableName);

        //将typeId转换为名称，方便和流程表进行对应

        for (DataType dataType : uncheckedOrder) {

            for (ProcessType type : types) {
                if(dataType.getType().equals(String.valueOf(type.getId()))){
                    dataType.setTypeName(type.getName());
                }
            }

        }

        //遍历需要进行流程的表单，找到具体进行的流程
        for (DataType dataType : uncheckedOrder) {

            List<ProcessCondition> conditions = conditionMapper.selectConditionByType(dataType.getTypeName());

            String conditionField = conditions.get(0).getConditionField();

            double number = dataMapper.getNumberByField(conditionField,dataTableName,dataType.getCode());

            //获取指定的流程线路

            long operationId = 0 ;

            if(number==Double.parseDouble(conditions.get(0).getEqualValue())){

                operationId = conditions.get(0).getEqualOperationId();

            }

            if(number<Double.parseDouble(conditions.get(0).getLessValue())){

                operationId = conditions.get(0).getLessOperationId();

            }

            if(number>Double.parseDouble(conditions.get(0).getMoreValue())){

                operationId = conditions.get(0).getMoreOperationId();

            }

            List<ProcessDetail> details = detailMapper.selectDetailsByRemarkAndType(operationId,dataType.getTypeName());

            //将流程插入运行表中
            for (ProcessDetail detail : details) {
                ProcessRun run = new ProcessRun();
                run.setRemark(dataType.getCode());
                run.setRole(detail.getRole());
                run.setPeople(detail.getPeople());
                run.setOperation(detail.getOperation());
                run.setContent(detail.getContent());
                run.setStepLevel(detail.getStepLevel());
                run.setStatus("0");

                runMapper.insertProcessRun(run);
            }

            //更新标签
            dataMapper.updateFlag(dataTableName,dataType.getCode());

        }


    }


    @Scheduled(cron = "0 */5 * * * ?") // 5分钟
    @Transactional
    public void runTable(){

        //查询需要运转的流程流程单号
        List<String> remarks = runMapper.getNeedRunRemark();

        for (String remark : remarks) {

            //查到流程单号下的第一级步骤未进行操作的步骤
            ProcessRun run = runMapper.getOperationStep(remark);

            //TODO 根据获取的流程单内数据调用消息接口和任务接口，以及目标人员消息提醒和任务提醒
            //todo 此处需要将runId传输过去，还需要判定任务或者消息接口是否已存在当id的消息，如果存在，则不需继续派发任务


        }

    }

}
