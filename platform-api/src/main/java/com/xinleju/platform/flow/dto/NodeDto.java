package com.xinleju.platform.flow.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程模板环节
 *
 */
public class NodeDto {
	private String Id;
	private String name;
	// 直接前序环节
	private List<AcDto> previousAcDtos = new ArrayList<AcDto>();
	// 当前环节
	private AcDto currentAcDto;
	// 直接后序环节
	private List<AcDto> nextNodeDtos = new ArrayList<AcDto>();

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public List<AcDto> getPreviousAcDtos() {
		return previousAcDtos;
	}

	public void setPreviousAcDtos(List<AcDto> previousAcDtos) {
		this.previousAcDtos = previousAcDtos;
	}

	public List<AcDto> getNextNodeDtos() {
		return nextNodeDtos;
	}

	public void setNextNodeDtos(List<AcDto> nextNodeDtos) {
		this.nextNodeDtos = nextNodeDtos;
	}

	public AcDto getCurrentAcDto() {
		return currentAcDto;
	}

	public void setCurrentAcDto(AcDto currentAcDto) {
		this.currentAcDto = currentAcDto;
	}


}
