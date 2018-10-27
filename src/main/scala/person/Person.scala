package person

import demographic._
import inventory.Inventory
import location.Location
import meal.Meal
import resource.{Carbs, Fat, Protein}
import status._

sealed trait Person {
  val name: String
  val inventory: Inventory
  val location: Location
  val health: HealthStatus
  val age: AgeBracket
  val gender: Gender
  def eat(): Person
  def act(): Person
  def relax(): Unit

  val caloriesRequired: Int = {
    (age, gender) match {
      case (Child, Female) => 1500
      case (Child, Male) => 1600
      case (Young, Female) => 1700
      case (Young, Male) => 1800
      case (Adult, Female) => 1900
      case (Adult, Male) => 2000
      case (Old, Female) => 1800
      case (Old, Male) => 1900
    }
  }
}


case class Commoner(name: String, inventory: Inventory,
                    location: Location, age: AgeBracket, gender: Gender,
                    health: HealthStatus = Fine) extends Person {
  override def eat(): Commoner = {
    val meal = candidateMeal(
      fromComponents = inventory,
      requiredCalories = caloriesRequired
    )

    meal match {
      case None => this.copy(health = health.nextWorst)
      case Some(eatenMeal) => this.copy(
        inventory = inventory.deductMeal(eatenMeal),
        health = health.nextBest
      )
    }
  }

  def farm(): Commoner = {
    println(s"$name is farming")
    val produce = Inventory(List(Protein, Fat, Carbs))
    val newInventory = inventory + produce
    this.copy(inventory = newInventory)
  }

  def candidateMeal(fromComponents: Inventory, requiredCalories: Int, size: Int = 1): Option[Meal] = {
    if (size >= fromComponents.size) {
      return None
    }

    val ingredients = fromComponents.randomSample(size)
    val meal = Meal.fromIngredients(ingredients)
    if (meal.totalCalories >= requiredCalories) {
      Some(meal)
    }
    else {
      candidateMeal(
        fromComponents = fromComponents,
        requiredCalories = requiredCalories,
        size = size + 1
      )
    }
  }

  def party(): Commoner = {
    relax()
    this
  }

  override def relax(): Unit = {
    println(s"$name is having a good time")
  }

  override def act(): Commoner = {
    val afterEating = eat()

    afterEating.health match {
      case Robust | Fine => afterEating.party()
      case Sick | Poor => afterEating.farm()
    }
  }
}