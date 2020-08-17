package com.xiaoshu.service;

import java.util.Date;
import java.util.List;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.StudentMapper;
import com.xiaoshu.dao.TeacherMapper;
import com.xiaoshu.dao.UserMapper;
import com.xiaoshu.entity.Student;
import com.xiaoshu.entity.StudentVo;
import com.xiaoshu.entity.Teacher;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;
import com.xiaoshu.entity.UserExample.Criteria;

@Service
public class StudentService {

	@Autowired
	private StudentMapper studentMapper;
	@Autowired
	private TeacherMapper teacherMapper;
	@Autowired
	private  JmsTemplate jmsTemplate;
	@Autowired
	private  Destination queueTextDestination;
	public PageInfo<StudentVo> findPage(StudentVo studentVo, Integer pageNum,Integer pageSize){
		PageHelper.startPage(pageNum, pageSize);
		List<StudentVo> list = studentMapper.findAll(studentVo);
		return new PageInfo<>(list);
	}
	public List<Teacher> findAllT(){
		return teacherMapper.selectAll();
	}
	public void updateS(Student studentVo){
		 studentMapper.updateByPrimaryKeySelective(studentVo);
		
	}
	public void addS(Student student){
		student.setCreatetime(new Date());
		studentMapper.insert(student);
	}
	public void addT(Teacher teacher){
		teacher.setCreatetime(new Date());
		teacherMapper.insert(teacher);
		String name = teacher.getName();
		Teacher param = new Teacher();
		param.setName(name);
		Teacher teacher2 = teacherMapper.selectOne(param );
		jmsTemplate.convertAndSend(queueTextDestination, JSONObject.toJSONString(teacher2));
	}
	public List<StudentVo> findE(){
		return studentMapper.findE();
	}

}
