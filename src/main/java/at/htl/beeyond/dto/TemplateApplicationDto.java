package at.htl.beeyond.dto;

import at.htl.beeyond.entity.Template;
import at.htl.beeyond.entity.TemplateApplication;
import at.htl.beeyond.entity.TemplateFieldValue;
import at.htl.beeyond.entity.User;
import at.htl.beeyond.validation.Exists;
import at.htl.beeyond.validation.TemplateFieldsComplete;
import at.htl.beeyond.validation.checks.TemplateFieldsCompleteChecks;
import org.hibernate.validator.constraints.Length;

import javax.json.bind.annotation.JsonbTransient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


@TemplateFieldsComplete(message = "Missing field ids: [{missing-ids}], Obsolete field ids: [{obsolete-ids}]", groups = TemplateFieldsCompleteChecks.class)
public class TemplateApplicationDto {

    private Long id;

    @Length(max = 255, message = "The note of the custom application cannot be longer than 255 characters")
    private String note;

    @NotNull(message = "The templateId of the template cannot be empty")
    @Exists(entity = Template.class, fieldName = "id", message = "The templateId of the template application does not exist")
    private Long templateId;

    @Valid
    private List<TemplateFieldValueDto> fieldValues = new LinkedList<>();

    private UserDto owner;

    public TemplateApplicationDto(Long id, String note, Long templateId, List<TemplateFieldValueDto> fieldValues) {
        this.id = id;
        this.note = note;
        this.templateId = templateId;
        this.fieldValues = fieldValues;
    }

    public TemplateApplicationDto() {
    }

    public Long getId() {
        return id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public List<TemplateFieldValueDto> getFieldValues() {
        return fieldValues;
    }

    public void setFieldValues(List<TemplateFieldValueDto> fieldValues) {
        this.fieldValues = fieldValues;
    }

    public UserDto getOwner() {
        return owner;
    }

    @Override
    public String toString() {
        return "";
    }

    @JsonbTransient
    public void setOwner(User user) {
        this.owner = UserDto.map(user);
    }

    public TemplateApplication map() {
        Template template = Template.findById(this.templateId);
        List<TemplateFieldValue> templateFieldValues = this.fieldValues.stream()
                .map(o -> o.map(template))
                .collect(Collectors.toList());

        return new TemplateApplication(this.note, this.owner.map(), template, templateFieldValues);
    }
}