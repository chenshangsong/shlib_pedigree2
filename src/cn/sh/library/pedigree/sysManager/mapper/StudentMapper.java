package cn.sh.library.pedigree.sysManager.mapper;

import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.sysManager.model.StudentEntity;

@Repository
public interface StudentMapper {

	public StudentEntity getStudent(String studentID);

	public StudentEntity getStudentAndClass(String studentID);

	/*public List<StudentEntity> getStudentAll();*/

	public void insertStudent(StudentEntity entity);

	public void deleteStudent(StudentEntity entity);

	public void updateStudent(StudentEntity entity);
}
