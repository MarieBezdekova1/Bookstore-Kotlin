package com.bezdekova.bookstore.model.command

// command pattern - spočívá v podstatě v tom, že do funkcí dáváš místo jednotlivých atributů jen jeden objekt
// to jen na ukázku, toto taky používáme
data class AuthorUpdateCommand(
  val id: String,
  val name: String
)
