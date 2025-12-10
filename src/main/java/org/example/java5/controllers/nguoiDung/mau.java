package org.example.java5.controllers.nguoiDung;

import org.example.java5.beans.studenbean;
import jakarta.validation.Valid;
import org.example.java5.entity.nganhhoc;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
@Controller
public class mau {
    @GetMapping("/mau")
    public String add( Model model) {
        List<nganhhoc> hoc= new ArrayList<>();
        hoc.add(new nganhhoc(1,"PTPM(java)"));
        hoc.add(new nganhhoc(2,"PTPM(C#)"));
        hoc.add(new nganhhoc(3,"game"));

        model.addAttribute("nganhhoc",hoc);



        return "nguoiDung/dangkymau";
    }
   @PostMapping("/dangky1-mau")
    public String handadd(@Valid studenbean studenbean, Errors errors, Model model) {

    model.addAttribute("studenbean",studenbean);


        return "nguoiDung/dangkymau.html";
    }

    @ModelAttribute("nganhhoc")
    public List<nganhhoc> hoc(){
    List<nganhhoc> hoc= new ArrayList<>();
    hoc.add(new nganhhoc(1,"PTPM(java)"));
    hoc.add(new nganhhoc(2,"PTPM(C#)"));
    hoc.add(new nganhhoc(3,"game"));

        return hoc;
    }


}
