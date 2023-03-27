package cn.sh.library.pedigree.sysManager;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;

public interface MySqlMapper {
	@Select("select * from MyTable")
	List<Map<String, Object>> getList();
}
