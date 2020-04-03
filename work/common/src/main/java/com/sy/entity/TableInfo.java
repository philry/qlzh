package com.sy.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class TableInfo
{
    private static final long serialVersionUID = 1L;

    private Long id;

    private String typeTableName;

    private String typeTableFiled;

    private String dataTableName;

    private String roleTableName;

    private String peopleTableName;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTypeTableName(String typeTableName) 
    {
        this.typeTableName = typeTableName;
    }

    public String getTypeTableName() 
    {
        return typeTableName;
    }
    public void setTypeTableFiled(String typeTableFiled) 
    {
        this.typeTableFiled = typeTableFiled;
    }

    public String getTypeTableFiled() 
    {
        return typeTableFiled;
    }
    public void setDataTableName(String dataTableName) 
    {
        this.dataTableName = dataTableName;
    }

    public String getDataTableName() 
    {
        return dataTableName;
    }
    public void setRoleTableName(String roleTableName) 
    {
        this.roleTableName = roleTableName;
    }

    public String getRoleTableName() 
    {
        return roleTableName;
    }
    public void setPeopleTableName(String peopleTableName) 
    {
        this.peopleTableName = peopleTableName;
    }

    public String getPeopleTableName() 
    {
        return peopleTableName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("typeTableName", getTypeTableName())
            .append("typeTableFiled", getTypeTableFiled())
            .append("dataTableName", getDataTableName())
            .append("roleTableName", getRoleTableName())
            .append("peopleTableName", getPeopleTableName())
            .toString();
    }
}
