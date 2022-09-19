package com.ISMM.common.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ISMM.common.model.IDBasedEntity;

@Entity
@Table(name="inventory")
public class InventoryItem extends IDBasedEntity{
	

	@Column(length=128, nullable=false, unique=false)
	private String name;
	
	@Column(length=64, nullable=false, unique=false)
	private String alias;
	
	@Column(length=128, nullable=false)
	private String image;
	
	private boolean enabled;
	
	@OneToOne()
	@JoinColumn(name="parent_id")
	private InventoryItem parent;
	
	@OneToMany(mappedBy = "parent")
	private Set<InventoryItem> children = new HashSet<>();

	
	public InventoryItem () {};
	
	public InventoryItem(Integer id) {
		super();
		this.id = id;
	}
	
	public InventoryItem(String name) {
		this.name = name;
		this.alias = name;
		this.image = "default.png";
	}
	public InventoryItem(String name, InventoryItem parent) {
		this(name);
		this.parent = parent;
	}
	
	
	
	public InventoryItem(Integer id, String name, String alias) {
		this.id = id;
		this.name = name;
		this.alias = alias;
	}

	public static InventoryItem copyIdAndName(InventoryItem category) {
		InventoryItem copyCategory = new InventoryItem();
		copyCategory.setId(category.getId());
		copyCategory.setName(category.getName());
		return copyCategory;
	}
	public static InventoryItem copyIdAndName(Integer id, String name) {
		InventoryItem copyCategory = new InventoryItem();
		copyCategory.setId(id);
		copyCategory.setName(name);
		return copyCategory;
	}
	
	public static InventoryItem copyFull(InventoryItem category) {
		InventoryItem copyCategory = new InventoryItem();
		copyCategory.setId(category.getId());
		copyCategory.setName(category.getName());
		copyCategory.setImage(category.getImage());
		copyCategory.setAlias(category.getAlias());
		copyCategory.setEnabled(category.isEnabled());
		copyCategory.setHasChildren(category.getChildren().size() > 0);

		
		return copyCategory;
	}
	
	public static InventoryItem copyFull(InventoryItem category, String name) {
		InventoryItem copyCategory = InventoryItem.copyFull(category);
		copyCategory.setName(name);
		return copyCategory;
	}

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public InventoryItem getParent() {
		return parent;
	}

	public void setParent(InventoryItem parent) {
		this.parent = parent;
	}

	public Set<InventoryItem> getChildren() {
		return children;
	}

	public void setChildren(Set<InventoryItem> children) {
		this.children = children;
	}

	@Transient
	public String getImagePath() {
		if (this.id == null) return "/images/image-thumbnail.png";

		return "../inventory-images/" + this.id + "/" + this.image;
	}
	
	public boolean isHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	@Transient
	private boolean hasChildren;

	
}
