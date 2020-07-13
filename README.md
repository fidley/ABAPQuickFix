# ABAP Quick Fix plugin for ADT
Installation from Marketplace https://marketplace.eclipse.org/content/abap-quick-fix 

Direct installation from http://abapblog.com/eclipse/plugin/ABAPFavorites/

ABAP Quick Fix
Functions so far:
- Remove all comments (do not delete pseudo comments and ADT comments)
- Remove all comments in selection (do not delete pseudo comments and ADT comments)
- Replace icon literals with constant
- Translate comments into English
- Replace READ TABLE with ASSIGN
- Replace READ TABLE with REF #( )
- Reaplace READ TABLE with Table Expression
- Replace CALL METHOD with direct call
- Reaplace MOVE with direct assignment
- Combine statements: DATA, TYPES, CONSTANTS, FIELD-SYMBOLS, CHECK, CLEAR, REFRESH, FREE, PARAMETERS, SELECT-OPTIONS, METHODS, CLASS-METHODS
- Change APPEND TO to APPEND VALUE #( ) TO
- Change INSERT INTO to INSERT VALUE #( ) INTO
- Split combined(chained) statements
- Remove Line Break at end of statement
- Replace CREATE OBJECT with NEW
- Omit EXPORTING in method call
- Omit RECEIVING in method call
- Change APPEND TO to INSERT VALUE #( ) INTO
- Replace GET REFERENCE with REF #
- Replace READ TABLE TRANSPORTING NO FIELDS with LINE_EXISTS
- Remove full line comments from statement
- Omit self reference ME->
