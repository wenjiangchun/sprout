package com.sprout.dlyy.work.entity;

import com.sprout.core.jpa.entity.AbstractBaseEntity;
import com.sprout.system.utils.ConfigType;

import javax.persistence.*;

@Entity
@Table(name="work_dairy")
public class WorkDairy extends AbstractBaseEntity<Long> {


	private static final long serialVersionUID = 1L;

    /**
     * 配置代码
     */
	private String code;

    /**
     * 配置名称
     */
	private String name;

    /**
     * 配置值
     */
	private String value;

    /**
     * 配置类型
     */
	private ConfigType configType = ConfigType.B;

    /**
     * 配置说明
     */
	private String description;

    @Column(unique=true, length = 20)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(length = 50)
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(length = 200)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Enumerated(EnumType.ORDINAL)
	public ConfigType getConfigType() {
		return configType;
	}

	public void setConfigType(ConfigType configType) {
		this.configType = configType;
	}

	@Column(length = 300)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Config [code=" + code + ", name="
				+ name + ", value=" + value + ", configType=" + configType.getTypeName()
				+ "]";
	}
	
}
