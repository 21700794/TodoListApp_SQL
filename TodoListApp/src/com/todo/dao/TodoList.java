package com.todo.dao;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.todo.service.TodoSortByDate;
import com.todo.service.TodoSortByName;
import java.io.BufferedReader;
import java.io.FileReader;

public class TodoList {
	private List<TodoItem> list;
	Connection conn;

	public TodoList() {
		this.conn = DbConnect.getConnection();
		this.list = new ArrayList<TodoItem>();
	}

	public int addItem(TodoItem t) {
		String sql = "insert into list(title, memo, category, current_date, due_date)"
				+ " values (? ,? ,? ,? ,?);";
		PreparedStatement pstmt;
		int count = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,t.getTitle());
			pstmt.setString(2,t.getDesc());
			pstmt.setString(3,t.getCategory());
			pstmt.setString(4,t.getCurrent_date());
			pstmt.setString(5,t.getDue_date());
			count=pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public int deleteItem(int index) {
		String sql = "delete from list where id=?;";
		PreparedStatement pstmt;
		int count = 0;
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1,index);
			count = pstmt.executeUpdate();
			pstmt.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
		return count;
	}

	public int editItem(TodoItem t) {
		String sql="update list set title=?, memo=?, category=?, current_date=?, due_date=?" + "where id=?;";
		PreparedStatement pstmt;
		int count=0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,t.getTitle());
			pstmt.setString(2,t.getDesc());
			pstmt.setString(3,t.getCategory());
			pstmt.setString(4,t.getCurrent_date());
			pstmt.setString(5,t.getDue_date());
			pstmt.setInt(6,t.getId());
			count = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public ArrayList<TodoItem> getList() {
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		Statement stmt;
		
		try {
			stmt = conn.createStatement();
			String sql="SELECT * FROM list ";
			ResultSet rs= stmt.executeQuery(sql);
			
			while (rs.next()) {
				int id = rs.getInt("id");
				String category = rs.getString("category");
				String title = rs.getString("title");
				String memo = rs.getString("memo");
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				TodoItem t = new TodoItem(title,memo,category,due_date);
				t.setId(id);
				t.setCurrent_date(current_date);
				list.add(t);
				
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public void sortByName() {
		Collections.sort(list, new TodoSortByName());

	}

	public void listAll() {
		for (TodoItem myitem : list) {
			System.out.println(myitem.toString());
		}
	}
	
	public void reverseList() {
		Collections.reverse(list);
	}

	public void sortByDate() {
		Collections.sort(list, new TodoSortByDate());
	}

	public int indexOf(TodoItem t) {
		return list.indexOf(t);
	}

	public boolean isDuplicate(String duplicate) {
		PreparedStatement pstmt;
		try {
			String sql = "SELECT * FROM list WHERE title = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, duplicate);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				String title = rs.getString("title");
				if(title.equals(duplicate))
					return true;
			}
			pstmt.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public int size() {
		return list.size();
	}
	
	public int getCount() {
		Statement stmt;
		int count = 0;
		try {
			stmt = conn.createStatement();
			String sql = "select count(id) from list;";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			count = rs.getInt("count(id)");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public void find(String line) {
		int count = 0;
		for (TodoItem myitem : list) {
			if (myitem.getTitle().contains(line)||myitem.getDesc().contains(line)) {
				System.out.println(myitem.toString());
				count++;
			}
		}
		System.out.printf("총 %d개의 항목을 찾았습니다!\n",count);
	}
	
	public void find_cate(String line) {
		int count = 0;
		for(TodoItem myitem:list) {
			if(myitem.getCategory().contains(line)) {
				System.out.println(myitem.toString());
				count++;
			}
			
		}
		System.out.printf("총 %d개의 항목을 찾았습니다!\n",count);
	}
	
	public ArrayList<String> getCategories(){
		ArrayList<String> list = new ArrayList<String>();
		Statement stmt;
		int count = 0;
		try {
			stmt = conn.createStatement();
			String sql = "SELECT DISTINCT category FROM list";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String category=rs.getString("category");
				System.out.print(category+" ");
				count++;
				
			}
			System.out.printf("\n총 %d개의 카테고리가 등록되어 있습니다.\n",count);
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList<TodoItem> getListCategory(String keyword){
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		PreparedStatement pstmt;
		try {
			String sql = "SELECT * FROM list WHERE category = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String category = rs.getString("category");
				String title = rs.getString("title");
				String memo = rs.getString("memo");
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				TodoItem t = new TodoItem(title,memo,category,due_date);
				t.setId(id);
				t.setCurrent_date(current_date);
				list.add(t);
				
			}
			pstmt.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public void importData(String filename) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line;
			String sql="insert into list (title,memo,category,current_date,due_date)"
					+ " values (?,?,?,?,?);";
			int records = 0;
			while((line=br.readLine())!= null) {
				StringTokenizer st = new StringTokenizer(line,"##");
				String category= st.nextToken();
				String title= st.nextToken();
				String description = st.nextToken();
				String due_date = st.nextToken();
				String current_date = st.nextToken();
				
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1,title);
				pstmt.setString(2,description);
				pstmt.setString(3,category);
				pstmt.setString(4,current_date);
				pstmt.setString(5,due_date);
				
				int count = pstmt.executeUpdate();
				if (count>0) records++;
				pstmt .close();
				
			}
			System.out.println(records+" records read!!");
			br.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<TodoItem> getOrderedList(String orderby,int ordering) {
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		Statement stmt;
		try {
			stmt = conn.createStatement();
			String sql = "select * from list order by "+ orderby;
			if (ordering == 0) {
				sql+=" desc";
			}
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				String category = rs.getString("category");
				String title = rs.getString("title");
				String memo = rs.getString("memo");
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				TodoItem t = new TodoItem(title,memo,category,due_date);
				t.setId(id);
				t.setCurrent_date(current_date);
				list.add(t);
				
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public void duplicate(int index) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select title, memo, category ,due_date , current_date, is_completed,percent,difficulty from list where id='" +  index + "'");
			while(rs.next()) {
				String title = rs.getString("title");
				String desc = rs.getString("memo");
				String category = rs.getString("category");
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				TodoItem t = new TodoItem(title, category, desc, due_date);
				t.setId(index);
				t.setCurrent_date(current_date);
				System.out.println(t.toString());
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

}
