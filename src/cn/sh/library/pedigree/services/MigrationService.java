package cn.sh.library.pedigree.services;


/**
 * Created by yesonme on 15-2-8.
 */
public interface MigrationService extends BaseService{
    //家族
    public String pedigree();

    //根据名人uri获取家族世系表
//    public String familyTimeline();

    //获取迁徙图地名
    public String migrationLocations();

    //获取迁徙图空间位置
    public String migrationGeoCoords();

    //获取迁徙路线
    public String migrationLines();
}
