package com.company.courtmanagement.service.util;

public final class ErrorMsgHelper {
    public static final String STAGE_NO_DOC = "Court stage should contain at least one document";
    public static final String STAGE_NO_COURT = "Court stage should contain court";
    public static final String COURT_NOT_EXIST = "No court with id ";
    public static final String CASE_NOT_EXIST = "No court case with id ";
    public static final String STAGE_NOT_EXIST = "No court stage with id ";

    private ErrorMsgHelper() {
    }
}
