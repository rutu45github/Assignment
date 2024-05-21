package com.example.animalapp.controller;

import com.example.animalapp.model.Animal;
import com.example.animalapp.repository.AnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/animals")
public class AnimalController {

    @Autowired
    private AnimalRepository animalRepository;

    @GetMapping("/form")
    public String showForm(Model model) {
        model.addAttribute("animal", new Animal());
        return "animalForm";
    }

    @PostMapping("/submit")
    public String submitAnimal(@Valid Animal animal, BindingResult result, MultipartFile file, Model model) {
        if (result.hasErrors()) {
            return "animalForm";
        }
        try {
            animal.setImage(file.getBytes());
            animal.setImageType(file.getContentType());
        } catch (IOException e) {
            result.rejectValue("image", "error.animal", "Failed to upload image");
            return "animalForm";
        }
        animalRepository.save(animal);
        return "redirect:/animals/list";
    }

    @GetMapping("/list")
    public String listAnimals(Model model) {
        List<Animal> animals = animalRepository.findAll();
        model.addAttribute("animals", animals);
        return "animalList";
    }

    @GetMapping("/edit/{id}")
    public String editAnimal(@PathVariable("id") long id, Model model) {
        Animal animal = animalRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid animal Id:" + id));
        model.addAttribute("animal", animal);
        return "animalForm";
    }

    @GetMapping("/delete/{id}")
    public String deleteAnimal(@PathVariable("id") long id) {
        Animal animal = animalRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid animal Id:" + id));
        animalRepository.delete(animal);
        return "redirect:/animals/list";
    }
}
