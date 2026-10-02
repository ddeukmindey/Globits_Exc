package com.globits.hr.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;
import com.globits.core.domain.Discipline;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("HRDiscipline")
public class HRDiscipline extends Discipline {
    private static final long serialVersionUID = 1L;

    @Column(name = "language_key")
    private String languageKey;

    @Column(name = "discipline_type")
    private Integer disciplineType;

    @Column(name = "formal")
    private Integer formal;

    @Column(name = "evaluate_year")
    private Integer evaluateYear;


    public String getLanguageKey() {
        return languageKey;
    }

    public void setLanguageKey(String languageKey) {
        this.languageKey = languageKey;
    }

    public Integer getDisciplineType() {
        return disciplineType;
    }

    public void setDisciplineType(Integer disciplineType) {
        this.disciplineType = disciplineType;
    }

    public Integer getFormal() {
        return formal;
    }

    public void setFormal(Integer formal) {
        this.formal = formal;
    }

    public Integer getEvaluateYear() {
        return evaluateYear;
    }

    public void setEvaluateYear(Integer evaluateYear) {
        this.evaluateYear = evaluateYear;
    }

}
