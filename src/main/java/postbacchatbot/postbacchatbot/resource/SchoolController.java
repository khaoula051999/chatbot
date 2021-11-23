package postbacchatbot.postbacchatbot.resource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import postbacchatbot.postbacchatbot.model.School;
import postbacchatbot.postbacchatbot.repository.SchoolRepository;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class SchoolController {

    @Autowired
    SchoolRepository repository ;




    @PostMapping("/addSchool")
    public  String saveSchool(@RequestBody School school){
        repository.save(school);
        return "Created school with id : " + school.getId();



    }

    @GetMapping("/findAllSchool")
    public List<School> getSchool(){
        return repository.findAll();

    }

    @GetMapping("/findAllSchool/{id}")
    public Optional<School> getSchool(@PathVariable int id){
        return  repository.findById(id);
    }


    @DeleteMapping("/deleteSchool/{id}")
    public String deleteSchool(@PathVariable int id){
        repository.deleteById(id);
        return "Discussion Deleted with id " + id ;
    }
}
