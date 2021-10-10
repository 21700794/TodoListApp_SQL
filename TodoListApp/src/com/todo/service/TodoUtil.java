package com.todo.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import com.todo.dao.DbConnect;

import com.todo.dao.TodoItem;
import com.todo.dao.TodoList;

public class TodoUtil {
	
	public static void createItem(TodoList list) {
		
		String title, desc, category, due_date;
		Scanner sc = new Scanner(System.in);
		
		System.out.println("항목추가\n" + "[제목] : ");
		
		title = sc.next();
		if (list.isDuplicate(title)) {
			System.out.printf("중복 불가!");
			return;
		}
		System.out.print("[카테고리] : ");
		category = sc.next().trim();
		sc.nextLine();
		System.out.print("[내용] : ");
		desc = sc.nextLine().trim();
		
		System.out.print("[마감일자] : ");
		due_date = sc.next().trim();
		
		TodoItem t = new TodoItem(title, desc, category, due_date);
		if (list.addItem(t) > 0)
			System.out.println("추가되었습니다.");
	}

	public static void deleteItem(TodoList l) {
		Scanner sc = new Scanner(System.in);
		
		System.out.print("[항목삭제]\n" + "삭제할 항목의 번호를 입력하시오 > ");
		
		int num = sc.nextInt();
		if (l.deleteItem(num)>0)
			System.out.println("삭제되었습니다.");
	}


	public static void updateItem(TodoList l) {
		
		String new_title, new_description, new_category, new_due_date;
		Scanner sc = new Scanner(System.in);

		System.out.print("[항목수정]\n"
				+ "수정할 항목의 번호을 입력하십시오 > "
			);
		int count = sc.nextInt();
		
		
		if (l.size() < count-1) {
			System.out.println("없는 번호입니다.");
			return;
		}
		
		System.out.print("[새 제목] : ");
		new_title = sc.next().trim();
		System.out.print("[새 카테고리] : ");
		new_category = sc.next().trim();
		sc.nextLine();
		System.out.print("[새 내용] : ");
		new_description = sc.nextLine().trim();
		
		System.out.print("[새 마감일자] : ");
		new_due_date = sc.next().trim();
		
		TodoItem t = new TodoItem(new_title, new_description, new_category, new_due_date);
		t.setId(count);
		if (l.editItem(t) > 0) System.out.println("수정 되었습니다.");

	}

	public static void listAll(TodoList l) {
		System.out.printf("[전체 목록, 총 %d개]\n", l.getCount());
		int i = 0;
		for (TodoItem item : l.getList()) {
			i++;
			System.out.println(i + item.toString());
		}
	}
	
	public static void listAll(TodoList l,String orderby,int ordering) {
		
		System.out.printf("[전체 목록, 총 %d개]\n",l.getCount());
		
		int i = 0;
		for (TodoItem item : l.getOrderedList(orderby, ordering)) {
			i++;
			System.out.println(i + item.toString());
		}
	}
	
	public static void saveList(TodoList l, String filename) {
		try { 
			Writer w = new FileWriter("todolist.txt");
			for(TodoItem item : l.getList()) {
				w.write(item.toSaveString());
			}
			
			System.out.println("정보 저장하였습니다.");
			w.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public static void loadList(TodoList l, String filename) {
		try {
			BufferedReader br = new BufferedReader(new FileReader("todolist.txt"));
			
			String oneline;
			while((oneline = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(oneline, "##");
				String category = st.nextToken();
				String title = st.nextToken();
				String desc = st.nextToken();
				String due_date = st.nextToken();
				
				TodoItem t = new TodoItem(title, desc, category, due_date);
				l.addItem(t);
			}
			br.close();
			System.out.println("로딩 완료하였습니다.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static void find(TodoList l, String keyword) {
		int count = 0;
		int i = 0;
		for(TodoItem item : l.getList() ) {
			i++;
			if(item.getDesc().contains(keyword)) {
				System.out.println(i + item.toString());
				count++;
			}
		}
		System.out.printf("총 %d개의 항목을 찾았습니다.\n", count);
	}
	
	public static void find_cate(TodoList l,String cate) {
		int count=0;
		for(TodoItem item:l.getListCategory(cate)) {
			System.out.println((count + 1) + item.toString());
			count++;
		}
		System.out.printf("\n총 %d개의 항목을 찾았습니다.\n", count);
		
		
	}
	
	public static void listCateAll(TodoList l) {
		
		for(String item:l.getCategories()) {
			System.out.print(item + " ");	
		}
	}
	
}
