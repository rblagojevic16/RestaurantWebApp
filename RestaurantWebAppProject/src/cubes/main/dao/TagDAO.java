package cubes.main.dao;

import java.util.List;

import cubes.main.entity.Tag;

public interface TagDAO {

	public List<Tag> getTagList();
	
	public List<Tag> getTagListWithProducts();
	
	public void saveTage(Tag tag);
	
	public void deleteTag(int id);
	
	public Tag getTag(int id);
	
	public List<Tag> getTagsById(List<Integer> ids);
		
}
