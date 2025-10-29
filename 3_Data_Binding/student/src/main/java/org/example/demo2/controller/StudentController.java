package org.example.demo2.controller;

import org.example.demo2.entity.Student;
import org.example.demo2.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private IStudentService studentService;

    @ModelAttribute("subjects")
    public List<String> getSubjects() {
        System.out.println("-------------Attribute run------------------------");
        return List.of("JS", "JAVA", "SQL", "PHP");
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String showList(ModelMap model) {
        List<Student> studentList = studentService.findAll();
        model.addAttribute("studentList", studentList);
        return "student/list";
    }

    @GetMapping("/add")
    public String showFormAdd(Model model) {
        model.addAttribute("student", new Student());
//        model.addAttribute("subjects",List.of("JS","JAVA","SQL","PHP"));
        return "student/add";
    }

    @PostMapping("/add")
    public String save(@ModelAttribute Student student,
                       RedirectAttributes redirectAttributes) {
        studentService.add(student);
        redirectAttributes.addFlashAttribute("mess", "Them moi thanh cong");
        return "redirect:/students";
    }

    @GetMapping("/detail")
    public String detail1(@RequestParam(name = "id", required = false, defaultValue = "3") int id,
                          Model model) {
        model.addAttribute("student", studentService.findById(id));
        return "/student/detail";

    }

    @GetMapping("/detail/{id:[12]}")
    public String detail2(@PathVariable(name = "id") int id,
                          Model model) {
        model.addAttribute("student", studentService.findById(id));
        return "/student/detail";

    }
}
