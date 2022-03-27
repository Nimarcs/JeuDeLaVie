import JeuDeLaVie._;
import org.scalatest.FunSuite;

class TestJeuDeLaVie extends FunSuite {

  /*
   * Question 1
   */

  test("chainesToGrille.normal") {
    val liste = List("XX ", " XX", " X ");
    val res = chainesToGrille(liste)
    val exp = List((0,0), (0,1), (1,1), (1,2), (2,1))
    assert(res === exp)
  }

  test("chainesToGrille.grand") {
    val liste = List("XX   X", " XX   ", "X   X ");
    val res = chainesToGrille(liste)
    val exp = List((0,0), (0,1),(0,5), (1,1), (1,2), (2,0), (2,4))
    assert(res === exp)
  }

  /*
   * Question 2
  */

  test("coinMinMax.normal") {
    val liste = List((0,0), (0,1),(0,5), (1,1), (1,2), (2,0), (2,4))
    val exp  =((0,0), (2,5))
    val res = coinMinMax(liste)
    assert(res === exp)
  }

  test("coinMinMax.inverse") {
    val liste = List((0,0), (0,1),(0,5), (1,1), (1,2), (2,0), (2,4)).reverse
    val exp  =((0,0), (2,5))
    val res = coinMinMax(liste)
    assert(res === exp)
  }

  test("coinMinMax.grand") {
    val liste = List((0,0), (0,1),(0,5), (1,1), (1,2), (2,0), (2,4), (Integer.MAX_VALUE, Integer.MIN_VALUE), (Integer.MIN_VALUE, Integer.MAX_VALUE) )
    val exp  =((Integer.MIN_VALUE,Integer.MIN_VALUE), (Integer.MAX_VALUE,Integer.MAX_VALUE))
    val res = coinMinMax(liste)
    assert(res === exp)
  }

  /*
   * Question 3
  */

  test("voisines8.normal") {
    val exp2 = List((-1,-1), (-1,0), (-1,1), (0,-1), (0,1), (1,-1), (1,0), (1,1))
    val res2 = voisines8( 0 , 0)
    assert(res2 === exp2)
    assert(res2.length == 8)

    val exp  = List((-7,-2), (-7,-1), (-7,0), (-6,-2), (-6,0), (-5,-2), (-5,-1), (-5,0))
    val res = voisines8( -6 , -1)
    assert(res === exp)
    assert(res.length == 8)
  }

  test("voisines8.grandOuPetit") {
    intercept[IndexOutOfBoundsException](voisines8( Integer.MAX_VALUE , 0))
    intercept[IndexOutOfBoundsException](voisines8( 0 , Integer.MAX_VALUE))
    intercept[IndexOutOfBoundsException](voisines8( Integer.MIN_VALUE , 0))
    intercept[IndexOutOfBoundsException](voisines8( 0, Integer.MIN_VALUE ))
    intercept[IndexOutOfBoundsException](voisines8( Integer.MAX_VALUE, Integer.MIN_VALUE ))
  }

  /*
   * Question 4
  */

  test("survivantes.normal") {
    val l = List((-1,1),(0,1), (1,2), (2,0), (2,1))
    val res = survivantes(l)
    val exp = List((0,1), (1,2), (2,1))
    assert(res === exp)
    assert(res.length <= l.length)
  }

  /*
   * Question 5
  */

  test("candidate.normal") {
    val l = List((-1,1),(0,1), (1,2), (2,0), (2,1))
    val res = candidates(l)

    val exp = List((-2,0),(-2,1),(-2,2),(-1,0),(-1,2),(0,0),(0,2),(0,3),(1,-1),(1,0),(1,1),(1,3),(2,-1),(2,2),(2,3),(3,-1),(3,0),(3,1),(3,2))

    assert(res === exp)
  }

  /*
   * Question 6
  */

  test("naissances.normal") {
    val l = List((-1,1),(0,1), (1,2), (2,0), (2,1))
    val res = naissances(l)

    val exp = List((0,2),(1,0))

    assert(res === exp)
  }

  /*
   * Question 8
  */

  test("voisines4.normal") {
    val exp2 = List((-1,0), (0,-1), (0,1), (1,0))
    val res2 = voisines4( 0 , 0)
    assert(res2 === exp2)
    assert(res2.length == 4)

    val exp  = List((-7,-1), (-6,-2), (-6,0), (-5,-1))
    val res = voisines4( -6 , -1)
    assert(res === exp)
    assert(res.length == 4)
  }

  test("voisines4.grandOuPetit") {
    intercept[IndexOutOfBoundsException](voisines4( Integer.MAX_VALUE , 0))
    intercept[IndexOutOfBoundsException](voisines4( 0 , Integer.MAX_VALUE))
    intercept[IndexOutOfBoundsException](voisines4( Integer.MIN_VALUE , 0))
    intercept[IndexOutOfBoundsException](voisines4( 0, Integer.MIN_VALUE ))
    intercept[IndexOutOfBoundsException](voisines4( Integer.MAX_VALUE, Integer.MIN_VALUE ))
  }

  /*
   * Question 9
  */

  test("naitJDLV.normal") {

    assert(naitJDLV(3) === true)
    assert(naitJDLV(2) === false)
    assert(naitJDLV(4) === false)
    assert(naitJDLV(-3) === false)

  }

  test("survitJDLV.normal") {

    assert(survitJDLV(3) === true)
    assert(survitJDLV(2) === true)
    assert(survitJDLV(1) === false)
    assert(survitJDLV(4) === false)
    assert(survitJDLV(-3) === false)

  }

  test("naitFredkin.normal") {

    assert(naitFredkin(3) === true)
    assert(naitFredkin(2) === false)
    assert(naitFredkin(1) === true)
    assert(naitFredkin(4) === false)
    assert(naitFredkin(-3) === true)

    //Fredkin : naissance ou survit sont contrains au même règle
    assert(naitFredkin(2) == survitFredkin(2))
    assert(naitFredkin(3) == survitFredkin(3))

  }

  test("survitFredkin.normal") {

    assert(survitFredkin(3) === true)
    assert(survitFredkin(2) === false)
    assert(survitFredkin(1) === true)
    assert(survitFredkin(4) === false)
    assert(survitFredkin(-3) === true)

    //Fredkin : naissance ou survit sont contrains au même règle
    assert(naitFredkin(2) == survitFredkin(2))
    assert(naitFredkin(3) == survitFredkin(3))

  }


}
