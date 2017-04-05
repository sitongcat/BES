package org.bes.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "costtype")
public class CostType {

	@Id
	@Column(name = "id", length = 40)
	private String id;
	@Column(name = "name", length = 200)
	private String name;
	@Column(name = "sort", columnDefinition = "int default 99")
	private Integer sort;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
}
