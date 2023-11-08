/***
 * The Chips class that stores the fields of the CSV file into 
 * separate data structures
 * @author ugola
 *
 */
public class Chips implements Comparable<Chips> {

	private String product;
	private String type;
	private String releaseDate;
	private String transistors;
	private int sortColumnNumber;
	/***
	 * Parameterized constructor for the Chips class
	 * @param product the product column of the CSV file
	 * @param type the type of the product e.g CPU or GPU
	 * @param releaseDate the release date of the product
	 * @param transistors the number of transistors in the product
	 * @param sortColumnNumber the field number on which the sorting is performed
	 * where the first field is 1
	 */
	public Chips(String product, String type, String releaseDate, String transistors, int sortColumnNumber) {
		super();
		this.product = product;
		this.type = type;
		this.releaseDate = releaseDate;
		this.transistors = transistors;
		this.sortColumnNumber = sortColumnNumber;
	}
	/***
	 * The getter method of field on which sorting is performed
	 * @return the field number
	 */
	public int getSortColumnNumber() {
		return sortColumnNumber;
	}
	/***
	 * The setter method of field on which sorting is performed
	 * @param sortColumnNumber the field number in int
	 */
	public void setSortColumnNumber(int sortColumnNumber) {
		this.sortColumnNumber = sortColumnNumber;
	}
	/***
	 * The getter method for product name 
	 * @return the product name in string
	 */
	public String getProduct() {
		return product;
	}
	/***
	 * The setter method of product name
	 * @param product name in string
	 */
	public void setProduct(String product) {
		this.product = product;
	}
	/***
	 * The getter method of type of the product
	 * @return the type of the product in string
	 */
	public String getType() {
		return type;
	}
	/***
	 * The setter method of the type of the product
	 * @param type the type of the product in string
	 */
	public void setType(String type) {
		this.type = type;
	}
	/***
	 * The getter method for the release date of the product
	 * @return the release date of the product in string
	 */
	public String getReleaseDate() {
		return releaseDate;
	}
	/***
	 * The setter method for the release date of the product
	 * @param releaseDate the release date of the product in string
	 */
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	/***
	 * The getter method of the number of transistors in the product
	 * @return the number of transistors in the product in string 
	 */
	public String getTransistors() {
		return transistors;
	}
	/***
	 * The setter method of the number of transistors in the product 
	 * @param transistors the number of transistors in string
	 */
	public void setTransistors(String transistors) {
		this.transistors = transistors;
	}
	/***
	 * This method overrides the toString method to have a naming convention for the fields
	 */
	@Override
	public String toString() {
		return "Chips [product=" + product + ", type=" + type + ", releaseDate=" + releaseDate + ", transistors="
				+ transistors + ", sortColumnNumber=" + sortColumnNumber + "]";
	}
	/***
	 * This method overrides the compareTo method in order to sort the fields 
	 */
	@Override 
	public int compareTo(Chips chip){
		 switch(chip.getSortColumnNumber()) {
		 	case 1:
		 		if (this.product.compareTo(chip.product) > 0) {
		 			// if current object is greater,then return 1
			        return 1;
			    } else if (this.product.compareTo(chip.product) < 0) {
			    	// if current object is greater,then return -1
			    	return -1;
			    } else {
			        // if current object is equal to o,then return 0
			        return 0;
			    }
			        
		 	case 2:
		 		if (this.type.compareTo(chip.type) > 0) {
			        // if current object is greater,then return 1
			        return 1;
			    } else if (this.type.compareTo(chip.type) < 0) {
			    	// if current object is greater,then return -1
			        return -1;
			    } else {
			    	// if current object is equal to o,then return 0
			        return 0;
			    }
			        
		 	case 3:
		 		if (this.releaseDate.compareTo(chip.releaseDate) > 0) {
		 			// if current object is greater,then return 1
			        return 1;
			    } else if (this.releaseDate.compareTo(chip.releaseDate) < 0) {
			    	// if current object is greater,then return -1
			        return -1;
			    } else {
			    	// if current object is equal to o,then return 0
			        return 0;
			    }
			       
			case 4:
				if (this.transistors.compareTo(chip.transistors) > 0) {
					// if current object is greater,then return 1
			        return 1;
			    } else if (this.transistors.compareTo(chip.transistors) < 0) {
			    	// if current object is greater,then return -1
			        return -1;
			    } else {
			    	// if current object is equal to o,then return 0
			        return 0;
			    }
		 }
		 	return -1;
	 }
	 /***
	  * This public method finds the HashCode of the fields
	  */
	 @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result + ((releaseDate == null) ? 0 : releaseDate.hashCode());
		result = prime * result + sortColumnNumber;
		result = prime * result + ((transistors == null) ? 0 : transistors.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	/***
	 * This public method overrides the equals method
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Chips other = (Chips) obj;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		if (releaseDate == null) {
			if (other.releaseDate != null)
				return false;
		} else if (!releaseDate.equals(other.releaseDate))
			return false;
		if (sortColumnNumber != other.sortColumnNumber)
			return false;
		if (transistors == null) {
			if (other.transistors != null)
				return false;
		} else if (!transistors.equals(other.transistors))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	/***
	 * the public constructor of Chips class
	 */
	public Chips() {
		
	}
}
