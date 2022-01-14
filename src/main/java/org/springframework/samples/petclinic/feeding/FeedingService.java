package org.springframework.samples.petclinic.feeding;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedingService {
	
	private FeedingRepository feedingRepo;
	
	@Autowired
	public FeedingService(FeedingRepository feedingRepo) {
		this.feedingRepo = feedingRepo;
	}
	
	@Transactional(readOnly = true)
    public List<Feeding> getAll(){
    	return feedingRepo.findAll();
    }

    public List<FeedingType> getAllFeedingTypes(){
        return feedingRepo.findAllFeedingTypes();
    }

    @Transactional(readOnly = true)
    public FeedingType getFeedingType(String typeName) {
        return feedingRepo.findFeedingTypeByName(typeName);
    }

    @Transactional(rollbackFor = UnfeasibleFeedingException.class)
    public Feeding save(Feeding p) throws UnfeasibleFeedingException {
    	if(!p.getPet().getType().equals(p.getFeedingType().getPetType())) {
    		throw new UnfeasibleFeedingException();
    	}
    	return feedingRepo.save(p);       
    }

    
}
