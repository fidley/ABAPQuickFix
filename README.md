# ABAP Quick Fix plugin for ADT
Installation from Marketplace https://marketplace.eclipse.org/content/abap-quick-fix 

![Eclipse Marketplace](https://img.shields.io/eclipse-marketplace/favorites/abap-quick-fix)

Direct installation from http://abapblog.com/eclipse/plugin/ABAPFavorites/

ABAP Quick Fix
Functions so far:
- Remove all comments (do not delete pseudo comments and ADT comments)
- Remove all comments in selection (do not delete pseudo comments and ADT comments)
- Replace icon literals with constant
- Translate comments into English
- Replace READ TABLE with ASSIGN
- Replace READ TABLE with REF #( )
- Replace READ TABLE with Table Expression
- Replace CALL METHOD with direct call
- Replace MOVE with direct assignment
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
- Replace operators EQ,NE,GT,GE,LT,LE with =,<>,>,>=,<,<=

![EQ QuickFix](https://user-images.githubusercontent.com/7912195/165106328-defeb765-ca3a-4c31-bd97-9791f1721cdd.gif)
- Align operators (in selected code)

  ![QuickFixAlignOperators](https://user-images.githubusercontent.com/7912195/165106135-20c72b55-224c-4bf5-bc87-4c517a23ab9d.gif)
- Replace Full Line Comment with In-line comment
- Align TYPEs(and LIKE) in variable definitions)
![alignTypes](https://user-images.githubusercontent.com/7912195/165099044-719f4d22-3548-4ff8-a733-931b1a4d63a4.gif)
