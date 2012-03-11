package org.inigma.shared.webapp;

import java.beans.PropertyEditor;
import java.util.List;
import java.util.Map;

import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public class RuntimeBindException extends RuntimeException implements BindingResult {
    private BindingResult ref;

    public RuntimeBindException(BindException source) {
        this.ref = source;
    }

    public RuntimeBindException(BindingResult source) {
        this.ref = source;
    }

    public RuntimeBindException(Object target, String name) {
        this.ref = new BeanPropertyBindingResult(target, name);
    }

    @Override
    public void addAllErrors(Errors errors) {
        ref.addAllErrors(errors);
    }

    @Override
    public void addError(ObjectError error) {
        ref.addError(error);
    }

    @Override
    public PropertyEditor findEditor(String field, Class<?> valueType) {
        return ref.findEditor(field, valueType);
    }

    @Override
    public List<ObjectError> getAllErrors() {
        return ref.getAllErrors();
    }

    @Override
    public int getErrorCount() {
        return ref.getErrorCount();
    }

    @Override
    public FieldError getFieldError() {
        return ref.getFieldError();
    }

    @Override
    public FieldError getFieldError(String field) {
        return ref.getFieldError(field);
    }

    @Override
    public int getFieldErrorCount() {
        return ref.getFieldErrorCount();
    }

    @Override
    public int getFieldErrorCount(String field) {
        return ref.getFieldErrorCount(field);
    }

    @Override
    public List<FieldError> getFieldErrors() {
        return ref.getFieldErrors();
    }

    @Override
    public List<FieldError> getFieldErrors(String field) {
        return ref.getFieldErrors(field);
    }

    @Override
    public Class<?> getFieldType(String field) {
        return ref.getFieldType(field);
    }

    @Override
    public Object getFieldValue(String field) {
        return ref.getFieldValue(field);
    }

    @Override
    public ObjectError getGlobalError() {
        return ref.getGlobalError();
    }

    @Override
    public int getGlobalErrorCount() {
        return ref.getGlobalErrorCount();
    }

    @Override
    public List<ObjectError> getGlobalErrors() {
        return ref.getGlobalErrors();
    }

    @Override
    public Map<String, Object> getModel() {
        return ref.getModel();
    }

    @Override
    public String getNestedPath() {
        return ref.getNestedPath();
    }

    @Override
    public String getObjectName() {
        return ref.getObjectName();
    }

    @Override
    public PropertyEditorRegistry getPropertyEditorRegistry() {
        return ref.getPropertyEditorRegistry();
    }

    @Override
    public Object getRawFieldValue(String field) {
        return ref.getRawFieldValue(field);
    }

    @Override
    public String[] getSuppressedFields() {
        return ref.getSuppressedFields();
    }

    @Override
    public Object getTarget() {
        return ref.getTarget();
    }

    @Override
    public boolean hasErrors() {
        return ref.hasErrors();
    }

    @Override
    public boolean hasFieldErrors() {
        return ref.hasFieldErrors();
    }

    @Override
    public boolean hasFieldErrors(String field) {
        return ref.hasFieldErrors(field);
    }

    @Override
    public boolean hasGlobalErrors() {
        return ref.hasGlobalErrors();
    }

    @Override
    public void popNestedPath() throws IllegalStateException {
        ref.popNestedPath();
    }

    @Override
    public void pushNestedPath(String subPath) {
        ref.pushNestedPath(subPath);
    }

    @Override
    public void recordSuppressedField(String field) {
        ref.recordSuppressedField(field);
    }

    @Override
    public void reject(String errorCode) {
        ref.reject(errorCode);
    }

    @Override
    public void reject(String errorCode, Object[] errorArgs, String defaultMessage) {
        ref.reject(errorCode, errorArgs, defaultMessage);
    }

    @Override
    public void reject(String errorCode, String defaultMessage) {
        ref.reject(errorCode, defaultMessage);
    }

    @Override
    public void rejectValue(String field, String errorCode) {
        ref.rejectValue(field, errorCode);
    }

    @Override
    public void rejectValue(String field, String errorCode, Object[] errorArgs, String defaultMessage) {
        ref.rejectValue(field, errorCode, errorArgs, defaultMessage);
    }

    @Override
    public void rejectValue(String field, String errorCode, String defaultMessage) {
        ref.rejectValue(field, errorCode, defaultMessage);
    }

    @Override
    public String[] resolveMessageCodes(String errorCode, String field) {
        return ref.resolveMessageCodes(errorCode, field);
    }

    @Override
    public void setNestedPath(String nestedPath) {
        ref.setNestedPath(nestedPath);
    }
}
