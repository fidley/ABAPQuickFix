package com.abapblog.adt.quickfix;

import java.util.ArrayList;

import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;

import com.abapblog.adt.quickfix.assist.syntax.statements.StatementAssist;

/**
 * Adds more quickfixes.
 * 
 * @author lc
 */
public interface IFixAppender {
    /**
     * Adds more quickfixes.
     * 
     * @param context
     * @return list of quickfixes
     */
    ArrayList<StatementAssist> additional_fixes(IQuickAssistInvocationContext context);
}
