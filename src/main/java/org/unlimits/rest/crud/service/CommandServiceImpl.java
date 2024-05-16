package org.unlimits.rest.crud.service;

public abstract class CommandServiceImpl<DT, EN, ID> implements CommandService<DT, EN, ID> {


	@Override
	public DT add(DT data) {
		EN mappedToDT = getMapper().mapToDAO(data);
		EN save = getRepository().save(mappedToDT);
		return getMapper().mapToDTO(save);
	}

	@Override
	public DT update(DT data) {
		EN mappedToDT = getMapper().mapToDAO(data);
		EN save = getRepository().save(mappedToDT);
		return getMapper().mapToDTO(save);
	}
	
	@Override
	public DT update(ID id, DT dto) {
		DT findById = find(id);
		if(findById==null) {
			return null;
		}
		return null;
	}

	@Override
	public Boolean delete(ID uuid) {
		getRepository().deleteById(uuid);
		return true;
	}
	
	@Override
	public DT find(ID uuid) {
		return getMapper().mapToDTO(getRepository().getReferenceById(uuid));
	}
}
