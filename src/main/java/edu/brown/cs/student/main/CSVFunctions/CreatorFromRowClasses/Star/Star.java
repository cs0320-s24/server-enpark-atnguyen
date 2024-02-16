package edu.brown.cs.student.main.CSVFunctions.CreatorFromRowClasses.Star;

/** A class that goes with StarCreator to make a Star object. */
public class Star {

  private float id;
  public String properName;
  public float x;
  private float y;
  private float z;

  /**
   * Basic star constructor to populate its fields with the properties passed into its constructor.
   *
   * @param id star id
   * @param properName star name
   * @param x its x coordinate
   * @param y its y coordinate
   * @param z its z coordinate
   */
  public Star(float id, String properName, float x, float y, float z) {
    this.id = id;
    this.properName = properName;
    this.x = x;
    this.y = y;
    this.z = z;
  }
}
