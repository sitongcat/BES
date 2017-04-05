package org.bes.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dept")
public class Dept {

    @Id
    @Column(name = "id", length = 40)
    private String id;
    @Column(name = "deptId", length = 40)
    private String deptId;
    @Column(name = "deptName", length = 50)
    private String deptName;
	@Column(name = "type", length = 10)
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


}
