package cn.sh.library.pedigree.webApi.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PlaceFacet {
	private String placeUri;
	private String place;
	private int count;
	
	private List<PlaceFacet> subFacets = new ArrayList<PlaceFacet>();

	public void addSub(PlaceFacet placeFacet){
		this.subFacets.add(placeFacet);
//		this.cntAll += placeFacet.getCount();
	}
	public void addSubs(List<PlaceFacet> placeFacets){
		this.subFacets.addAll(placeFacets);
	}
	public List<PlaceFacet> getSubPlaceFacets() {
		Collections.sort(subFacets, new Comparator<PlaceFacet>() {  
			@Override
			public int compare(PlaceFacet o1, PlaceFacet o2) {
				return o2.getCount() - o1.getCount();
			}  
        });
		return subFacets;
	}
	public void setSubPlaceFacets(List<PlaceFacet> subPlaceFacets) {
		this.subFacets = subPlaceFacets;
	}
	public String getPlaceUri() {
		return placeUri;
	}
	public void setPlaceUri(String placeUri) {
		this.placeUri = placeUri;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
//		this.cntAll = count;
		this.count = count;
	}
}
