package org.springframework.samples.petclinic.feeding;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.pet.Pet;
import org.springframework.samples.petclinic.pet.PetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FeedingController {
	
	private static final String VIEWS_FEEDINGS_CREATE_OR_UPDATE_FORM = "feedings/createOrUpdateFeedingForm";
	
	private FeedingService feedingService;
	private PetService petService;
	
	@Autowired
	public FeedingController(FeedingService feedingService, PetService petService) {
		this.feedingService = feedingService;
		this.petService = petService;
	}
	
	@GetMapping(value = "/feeding/create")
	public String initCreationForm(ModelMap model) {
		Feeding feeding = new Feeding();
		model.put("feeding", feeding);
		List<Pet> pets = petService.getAllPets();
		model.put("pets", pets);
		List<FeedingType> feedingTypes = feedingService.getAllFeedingTypes();
		model.put("feedingTypes", feedingTypes);
		return VIEWS_FEEDINGS_CREATE_OR_UPDATE_FORM;
	}
	
	@PostMapping(value = "/feeding/create")
	public String processCreationForm(@Valid Feeding feeding, BindingResult result, ModelMap model) {		
		if (result.hasErrors()) {
			model.put("feeding", feeding);
			model.put("feeding", feeding);
			List<Pet> pets = petService.getAllPets();
			model.put("pets", pets);
			List<FeedingType> feedingTypes = feedingService.getAllFeedingTypes();
			model.put("feedingTypes", feedingTypes);
			return VIEWS_FEEDINGS_CREATE_OR_UPDATE_FORM;
		}
		else {
            try{
            	this.feedingService.save(feeding);
            }catch(UnfeasibleFeedingException ex){
                result.rejectValue("pet", "error", "La mascota seleccionada no se le puede asignar el plan de alimentación especificado.");
                List<Pet> pets = petService.getAllPets();
    			model.put("pets", pets);
    			List<FeedingType> feedingTypes = feedingService.getAllFeedingTypes();
    			model.put("feedingTypes", feedingTypes);
                return VIEWS_FEEDINGS_CREATE_OR_UPDATE_FORM;
            }
            return "redirect:/welcome";
		}
		
	}
}
