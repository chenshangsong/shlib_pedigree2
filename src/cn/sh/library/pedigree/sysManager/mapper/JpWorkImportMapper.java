package cn.sh.library.pedigree.sysManager.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.sysManager.model.JpPersonModel;
import cn.sh.library.pedigree.sysManager.model.JpWorkModel;

@Repository
public interface JpWorkImportMapper {

	List<JpWorkModel> getWorkList();

	List<JpWorkModel> getDOIAccesslevelList();

	List<JpPersonModel> getXzmrList();

	List<JpPersonModel> getZrzList();

	List<JpPersonModel> getZrzQtList();

}
