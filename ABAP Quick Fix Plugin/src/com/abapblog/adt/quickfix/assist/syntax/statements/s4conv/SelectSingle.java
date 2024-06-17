package com.abapblog.adt.quickfix.assist.syntax.statements.s4conv;

import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;
import com.abapblog.adt.quickfix.assist.syntax.statements.StatementAssist;

public class SelectSingle extends StatementAssist implements IAssistRegex {

	private static final String ORDER_BY_PRIMARY_KEY = "order by primary key.";
//    private static final String WBHI_ORDER = "order by tkonn, tposn.\n";
//    private static final String WBGT_ORDER = "order by doc_type, vbeln, posnr, posnr_sub, gjahr. \"\" old key fields";
//    private static final String WBHF_ORDER = "order by tkonn_from, tposn_from, tposn_sub_from, tkonn_to, tposn_to, tktyp_to.";
//    private static final String WBIT_ORDER = "order by doc_type, doc_nr, doc_year, item, sub_item.";
//    private static final String WBHD_ORDER = "order by tkonn, tposn, tposn_sub.";
//    private static final String ASSO_ORDER1 = "order by tew_type, assoc_step_from, rdoc_nr, rdoc_year, rdoc_bukrs, rposnr, rposnr_sub,";
//    private static final String ASSO_ORDER2 = "         assoc_step_to, adoc_nr, adoc_year, adoc_bukrs, aposnr, aposnr_sub, rec_base.";
//    private static final String EKBE_ORDER = "order by ebeln ebelp zekkn vgabe gjahr belnr buzei.";
//    private static final String VBFA_ORDER = "order by vbelv, posnv, vbeln, posnn, vbtyp_n.";
//    private static final String EINE_ORDER = "order by  infnr, ekorg, esokz, werks.";
//    private static final String KONV_ORDER = "order by knumv, kposn, stunr, zaehk. ";
//    private static final String MVKE_ORDER = "order by matnr, vkorg, vtweg.";
//    private static final String DRAD_ORDER = "order by dokar, doknr, dokvr, doktl, dokob, objky, obzae. \"#EC CI_FLDEXT_OK";

	private static final String selectPattern =
			// select single * from wbhk into @data(result) where tkonn = ''.
			// 1 2 3 4 5 6
			"(?s)(\\s*)select\\s+(single)\\s+(.*)\\s+into\\s+(.*)\\s+from\\s+(.*)\\s+where\\s+(.*)";

	private static final String replaceByNewSelectPattern1 = "select $3 from $4";
	private static final String replaceByNewSelectPattern2 = "into $5";
	private static final String replaceByNewSelectPattern3 = "up to 1 rows";
	private static final String replaceByNewSelectPattern4 = "where";
	private static final String replaceByNewSelectPattern5 = "  $6";
	private static final String replaceByNewSelectPatternEnd = "endselect";
	private String currentTable;
	/**
	 * already contains line break
	 */
	private String currentIndent;
	private boolean comments = false;
	private int indent_number = 2;

	@Override
	public String getChangedCode() {
		String ChangedCode = "";
		String temp = CodeReader.CurrentStatement.replaceAllPattern("(.*)(\r\n)(.*)", "$1$3");
		temp = temp.trim().replaceAll(" +", " "); // condense spaces
		temp = temp.trim().replaceAll("\\R+", ""); // remove line breaks
		// line breaks are added automatically with the indentation prefix

		return CodeReader.CurrentStatement.getStatement()
				.concat(temp.replaceFirst(getMatchPattern(), getReplacePattern()));
//		return StringCleaner.clean(BeginningOfStatement + ChangedCode + EndOfStatement);
	}

	@Override
	public String getAssistShortText() {
		return "Replace select single with select up to one rows";
	}

	@Override
	public String getAssistLongText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canAssist() {
		if (CodeReader.CurrentStatement.matchPattern(getMatchPattern())) {
			// table name to decide on order by clause
			currentTable = CodeReader.CurrentStatement.replacePattern(getMatchPattern(), "$4").replaceAll("\\R", "")
					.replaceAll("\s", "");
			// get current indentation for select, prefix with linebreak
			String temp = CodeReader.CurrentStatement.replacePattern(getMatchPattern(), "$1");
			currentIndent = "\r\n".concat(temp.replaceAll("\\s*\\R", ""));// remove spaces
			return true;
		}
		return false;
	}

	@Override
	public int getStartOfReplace() {
		return CodeReader.CurrentStatement.getBeginOfStatementReplacement();
	}

	@Override
	public int getReplaceLength() {
		return CodeReader.CurrentStatement.getStatementLength();
	}

	@Override
	public String getMatchPattern() {
		return selectPattern;

	}

	@Override
	public String getReplacePattern() {
		String indent = getPrefix();
		StringBuffer temp = new StringBuffer();
		temp.append(currentIndent.concat(replaceByNewSelectPattern1));// select
		temp.append(currentIndent.concat(indent).concat(replaceByNewSelectPattern2)); // into
		temp.append(currentIndent.concat(indent).concat(replaceByNewSelectPattern3)); // up to 1 rows
		temp.append(currentIndent.concat(indent).concat(replaceByNewSelectPattern4)); // where
		temp.append(currentIndent.concat(indent).concat(replaceByNewSelectPattern5)); // statement

		// order by depends on table
		// several tables feature uuids as keys - use old key fields to order lines
		// order by primary key otherwise
		String orderBy = null;

		if (orderBy != null) {
			temp.append(currentIndent.concat(indent).concat("order by " + orderBy + "."));
//        } else if (currentTable.toLowerCase().contains("wbgt")) {
//            temp.append(currentIndent.concat(indent).concat(WBGT_ORDER));
//        } else if (currentTable.toLowerCase().contains("wbhf")) {
//            temp.append(currentIndent.concat(indent).concat(WBHF_ORDER));
//        } else if (currentTable.toLowerCase().contains("ekbe")) {
//            temp.append(currentIndent.concat(indent).concat(EKBE_ORDER));
//        } else if (currentTable.toLowerCase().contains("vbfa")) {
//            temp.append(currentIndent.concat(indent).concat(VBFA_ORDER));
//        } else if (currentTable.toLowerCase().contains("konv")) {
//            temp.append(currentIndent.concat(indent).concat(KONV_ORDER));
//        } else if (currentTable.toLowerCase().contains("drad")) {
//            temp.append(currentIndent.concat(indent).concat(DRAD_ORDER));
//        } else if (currentTable.toLowerCase().contains("mvke")) {
//            temp.append(currentIndent.concat(indent).concat(MVKE_ORDER));
//        } else if (currentTable.toLowerCase().contains("wbhd")) {
//            temp.append(currentIndent.concat(indent).concat(WBHD_ORDER));
//        } else if (currentTable.toLowerCase().contains("wbit")) {
//            temp.append(currentIndent.concat(indent).concat(WBIT_ORDER));
//        } else if (currentTable.toLowerCase().contains("eine")) {
//            temp.append(currentIndent.concat(indent).concat(EINE_ORDER));
//        } else if (currentTable.toLowerCase().contains("wbassoc")) {
//            temp.append(currentIndent.concat(indent).concat(ASSO_ORDER1 + currentIndent + ASSO_ORDER2));
////		} else if(currentTable.toLowerCase().contains("")){
////			temp.append( currentIndent.concat("  ") );
		} else {
			temp.append(currentIndent.concat(indent).concat(ORDER_BY_PRIMARY_KEY));
		}
		temp.append(currentIndent.concat(replaceByNewSelectPatternEnd));
		return temp.toString();

	}

	private String getPrefix() {
		return String.format("%" + indent_number + "s", "");
	}

}
