package cn.sh.library.pedigree.framework.baseDto;

import java.util.ArrayList;

import cn.sh.library.pedigree.framework.controller.BaseController;
import cn.sh.library.pedigree.framework.interfaces.IfValidator;
import cn.sh.library.pedigree.framework.model.BaseDto;
import cn.sh.library.pedigree.framework.model.DtoMessage;
/**
 * 类名 : BaseDtoValidator <br>
 * 机能概要 : </br> 版权所有: Copyright © 2011 TES Corporation, All Rights
 * Reserved.</br>
 * 
 * @author chenshangsong
 * @version 1.0
 * 
 */
public class BaseDtoValidator implements IfValidator {
	protected BaseController bctrl;
	protected ArrayList<DtoMessage> listMsg = new ArrayList<DtoMessage>();

	@Override
	public void validate(BaseDto arg0) throws Exception {
		listMsg.clear();
		BaseDto dto = (BaseDto) arg0;
		simpleCheck(dto);
		if (listMsg.size() <= 0) {
			businessCheck(dto);
		}

	}

	public BaseController getController() {
		return bctrl;
	}

	public void setController(BaseController bctrl) {
		this.bctrl = bctrl;
	}

	public ArrayList<DtoMessage> getListMsg() {
		return listMsg;
	}

	@Override
	public void simpleCheck(BaseDto dto)  throws Exception{
		// TODO Auto-generated method stub

	}

	@Override
	public void businessCheck(BaseDto dto) throws Exception {
		// TODO Auto-generated method stub

	}

}
