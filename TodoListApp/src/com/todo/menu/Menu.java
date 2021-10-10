package com.todo.menu;
public class Menu {

    public static void displaymenu()
    {
        System.out.println();
        System.out.println("[ToDoList 명령어]");
        System.out.println("1. add ( 항목 추가 )");
        System.out.println("2. del ( 항목 삭제 )");
        System.out.println("3. edit ( 항목 수정 )");
        System.out.println("4. ls ( 전체 목록 )");
        System.out.println("5. ls_name ( 제목순 정렬 )");
        System.out.println("6. ls_name_desc ( 제목역순 정렬 )");
        System.out.println("7. ls_date ( 날짜순 정렬 )");
        System.out.println("8. ls_date_desc (날짜역순 정렬)");
        System.out.println("9. save ( 저장 )");
        System.out.println("10. load ( 불러오기 )");
        System.out.println("11. find ( 검색 )");
        System.out.println("12. find_cate (카테고리 찾기)");
        System.out.println("13. ls_cate (카테고리 정렬)");
        System.out.println("0. exit ( 종료 )");
 
    }
    public static void prompt()
    {
    	System.out.println("\n입력하십시오 > ");
    }
}
