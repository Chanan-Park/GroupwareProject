package groupware;

public class DepartmentsDTO { // tbl_departments

	private String department_id; // 부서번호(기본키)
	private String department_name; // 부서이름
	
	public String getDepartment_id() {
		return department_id;
	}
	public void setDepartment_id(String department_id) {
		this.department_id = department_id;
	}
	public String getDepartment_name() {
		return department_name;
	}
	public void setDepartment_name(String department_name) {
		this.department_name = department_name;
	}
}
