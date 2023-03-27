package cn.sh.library.pedigree.framework.database;

import org.springframework.transaction.TransactionStatus;


/**
 * 类名 : DbTransaction <br>
 * 机能概要 : </br> 
 * 版权所有: Copyright © 2011 TES Corporation, All Rights Reserved.</br> 
 * 
 * @author chenshangsong
 * @version 1.0
 * 
 */
public interface DbTransaction {
	void begin();
	void commit();
	void rollback();
	void close();
	public TransactionStatus getStatus();
}
