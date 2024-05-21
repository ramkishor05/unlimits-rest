package org.unlimits.rest.crud.service;

import java.util.List;
import java.util.Map;

public abstract class CommandServiceImpl<DT, EN, ID> implements CommandService<DT, EN, ID> {


	@Override
	public DT add(DT data, Map<String, List<String>> headers) {
		EN mappedToDT = getMapper().mapToDAO(data);
		EN save = getRepository().save(mappedToDT);
		return getMapper().mapToDTO(save);
	}

	@Override
	public DT update(DT data, Map<String, List<String>> headers) {
		EN mappedToDT = getMapper().mapToDAO(data);
		EN save = getRepository().save(mappedToDT);
		return getMapper().mapToDTO(save);
	}
	
	@Override
	public DT update(ID id, DT dto, Map<String, List<String>> headers) {
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
