package com.ssbusy.core.region.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.broadleafcommerce.common.media.domain.Media;
import org.broadleafcommerce.common.media.domain.MediaImpl;
import org.broadleafcommerce.common.presentation.AdminPresentationMap;
import org.broadleafcommerce.common.presentation.AdminPresentationMapKey;
import org.broadleafcommerce.core.catalog.domain.CategoryImpl.Presentation;
import org.broadleafcommerce.inventory.domain.FulfillmentLocation;
import org.broadleafcommerce.inventory.domain.FulfillmentLocationImpl;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "B_REGION")
public class RegionImpl implements Region {
	@Id
	@Column(name = "REGION_ID")
	protected Long id;
	@Column(name = "REGION_NAME")
	protected String regionName;

	/**
	 * 一个校区可以有多个仓供应，一个仓颗供应多个校区
	 */
	// @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE },
	// targetEntity = FulfillmentLocationImpl.class, optional = true)
	// @JoinColumn(name = "FULFILLMENT_LOCATION_ID")
	@ManyToMany(targetEntity = FulfillmentLocationImpl.class, fetch = FetchType.EAGER)
	@OrderColumn
	@JoinTable(name = "SSB_REGION_LOCATION_XREF", joinColumns = @JoinColumn(name = "REGION_ID", nullable = false), inverseJoinColumns = @JoinColumn(name = "FULFILLMENT_LOCATION_ID", nullable = false))
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blStandardElements")
	@BatchSize(size = 50)
	protected List<FulfillmentLocation> fulfillmentLocations;

	/*
	 * 插入配送时间，格式为10:00-12:50;14:00-19:30；20:00-22:00（分割符为-和；，时间自己手动添加）
	 */
	@Column(name = "SHIPPING_TIME")
	protected String shipping_time;

	@Column(name = "SHORT_NAME")
	protected String shortName;
	
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@SuppressWarnings("deprecation")
	@ManyToMany(targetEntity = MediaImpl.class)
    @JoinTable(name = "BLC_REGION_MEDIA_MAP", inverseJoinColumns = @JoinColumn(name = "MEDIA_ID", referencedColumnName = "MEDIA_ID"))
    @MapKeyColumn(name = "MAP_KEY")
    @Cascade(value={org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blStandardElements")
    @BatchSize(size = 50)
    @AdminPresentationMap(
            friendlyName = "校区图片",
            tab = Presentation.Tab.Name.Media, tabOrder = Presentation.Tab.Order.Media,
            keyPropertyFriendlyName = "RegionImpl_Region_Media_Key",
            deleteEntityUponRemove = true,
            mediaField = "url",
            keys = {
                    @AdminPresentationMapKey(keyName = "primary", friendlyKeyName = "mediaPrimary"),
                    @AdminPresentationMapKey(keyName = "alt1", friendlyKeyName = "mediaAlternate1"),
                    @AdminPresentationMapKey(keyName = "alt2", friendlyKeyName = "mediaAlternate2"),
                    @AdminPresentationMapKey(keyName = "alt3", friendlyKeyName = "mediaAlternate3"),
                    @AdminPresentationMapKey(keyName = "alt4", friendlyKeyName = "mediaAlternate4"),
                    @AdminPresentationMapKey(keyName = "alt5", friendlyKeyName = "mediaAlternate5"),
                    @AdminPresentationMapKey(keyName = "alt6", friendlyKeyName = "mediaAlternate6")
            }
    )
    protected Map<String, Media> regionMedia = new HashMap<String , Media>(10);
	
	public String getShipping_time() {
		return shipping_time;
	}

	public void setShipping_time(String shipping_time) {
		this.shipping_time = shipping_time;
	}

	public List<FulfillmentLocation> getFulfillmentLocations() {
		return fulfillmentLocations;
	}

	public void setFulfillmentLocations(
			List<FulfillmentLocation> fulfillmentLocations) {
		this.fulfillmentLocations = fulfillmentLocations;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public Map<String, Media> getRegionMedia() {
		return regionMedia;
	}

	public void setRegionMedia(Map<String, Media> regionMedia) {
		 this.regionMedia.clear();
	        for(Map.Entry<String, Media> me : regionMedia.entrySet()) {
	            this.regionMedia.put(me.getKey(), me.getValue());
	        }
	}

	@Override
	public String toString() {
		return regionName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((regionName == null) ? 0 : regionName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RegionImpl other = (RegionImpl) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (regionName == null) {
			if (other.regionName != null)
				return false;
		} else if (!regionName.equals(other.regionName))
			return false;
		return true;
	}
}
