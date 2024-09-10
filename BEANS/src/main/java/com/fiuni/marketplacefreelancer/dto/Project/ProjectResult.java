package com.fiuni.marketplacefreelancer.dto.Project;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiuni.marketplacefreelancer.dto.base.BaseResult;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "projectResult")
public class ProjectResult extends BaseResult<ProjectDTO> {

    private static final long serialVersionUID = 1L;

    @XmlElement
    @JsonProperty("projects")
    public List<ProjectDTO> getProjects() {
        return getList();
    }

    public void setProjects(List<ProjectDTO> projects) {
        super.setList(projects);
    }

}
