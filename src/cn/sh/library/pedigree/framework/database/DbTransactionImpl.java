package cn.sh.library.pedigree.framework.database;
import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;



/**
 * 类名 : DbTransactionImpl <br>
 * 机能概要 : </br> 
 * 版权所有: Copyright © 2011 TES Corporation, All Rights Reserved.</br> 
 * 
 * @author chenshangsong
 * @version 1.0
 * 
 */
public class DbTransactionImpl implements DbTransaction {

	private static final Logger logger = Logger
	.getLogger(DbTransactionImpl.class);
	
	private DataSourceTransactionManager txManager;
	private TransactionStatus status;

	public void setTxManager(DataSourceTransactionManager txManager) {
		this.txManager = txManager;
	}
	
	@Override
	public void begin() {
    	DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		status = txManager.getTransaction(def);
	}

	@Override
	public void commit() {
		try{
			txManager.commit(status);			
		}catch(Exception ex){
			logger.error(ex.getMessage());
		}
	}

	@Override
	public void rollback() {
		try{
			txManager.rollback(status);
		}catch(Exception ex){
			logger.error(ex.getMessage());
		}
	}

	@Override
	public void close() {
	}

	public TransactionStatus getStatus() {
		return status;
	}

}
