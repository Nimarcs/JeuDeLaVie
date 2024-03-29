import scala.annotation.tailrec

object JeuDeLaVie {

  /*
   * Question 1
   */
  type Grille = List[(Int,Int)]

  /**
   * transforme une chaine en une Grille
   */
  def chaineToGrille(s: List[Char], ligne: Int, colonne: Int): Grille = s match {
    case t::q if t==' ' => chaineToGrille(q, ligne, colonne + 1)
    case t::q if t=='X' => (ligne, colonne) :: chaineToGrille(q, ligne, colonne + 1)
    case Nil => Nil
    case _ => throw new IllegalStateException
  }

  /**
   * transforme une liste de chaine en une Grille
   */
  def chainesToGrille(l:List[String]):Grille =  {
    def aux(l:List[String], ligne:Int):Grille = l match {
      case t::q => (chaineToGrille(t.toList, ligne, 0) ++ aux(q, ligne+1))
      case Nil => List[(Int, Int)]()
    }
    aux(l, 0)
  }

  /*
   * Question 2
   */

  /**
   * Cherche le coin minimum et maximum
   * en un parcours
   */
  def coinMinMax(g:Grille):((Int, Int), (Int, Int)) = {
    val (l, c) = g.head
    def nouvelleVal(anc:Int, nouv:Int, comparateur:(Int, Int)=>Boolean) = if(comparateur(anc, nouv)) nouv else  anc
    def nouveauCoin(anc:(Int, Int), nouv:(Int, Int), comparateur:(Int, Int)=>Boolean): (Int, Int)={
      val (ancl, ancc) = anc
      val (nouvl, nouvc) = nouv
      (nouvelleVal(ancl, nouvl, comparateur),  nouvelleVal(ancc, nouvc, comparateur))
    }
    def minMax(acc: ((Int, Int), (Int, Int)), e:(Int, Int)) = {
      val ((lmin, cmin), (lmax, cmax)) = acc
      val (l:Int, c:Int) = e
      (  nouveauCoin((lmin, cmin), (l, c), (a, n)=>a>n)  , nouveauCoin((lmax, cmax), (l, c), (a, n)=>a<n) )
    }
    (g foldLeft ((l, c), (l, c)))(minMax)
  }

  /**
   * Affiche la grille dans la console
   */
  def afficherGrille(g:Grille):Unit = {
    if (g.nonEmpty) {
      val ((lmin, cmin), (lmax, cmax)) = coinMinMax(g)

      def afficher(ligne: Int, colonne: Int, g: Grille): Unit = g match {
        case _ if ligne > lmax =>
        case v if colonne > cmax => {
          print("\n")
          afficher(ligne + 1, cmin, v)
        }
        case t :: q => {
          val (l, c) = t
          if (ligne == l && colonne == c) {
            print("X")
            afficher(ligne, colonne + 1, q)
          } else {
            print(" ")
            afficher(ligne, colonne + 1, t :: q)
          }
        }
        case Nil => {
          print(" ");
          afficher(ligne, colonne + 1, Nil)
        }
      }

      afficher(lmin, cmin, g)
    }
  }

  /*
   * Question 3
   */
  def estOOB(l:Int, c:Int) = l == Integer.MAX_VALUE || l == Integer.MIN_VALUE || c == Integer.MAX_VALUE || c == Integer.MIN_VALUE
  def voisines8(l:Int, c:Int):List[(Int, Int)] = {
    if (estOOB(l,c)) throw new IndexOutOfBoundsException
    List((l-1, c-1),(l-1, c), (l-1, c+1), (l, c-1),(l, c+1), (l+1, c-1), (l+1, c), (l+1, c+1))
  }

  /*
   * Question 4
   */
  def nbVivanteParmiVoisines(g:Grille, voisines:Grille):Int={
    (g foldLeft 0)((acc, e)=> {
      //nbElement a 0 ou 1
      val nbElement = (voisines foldLeft 0)((accV, eV)=> {
        val (lv, cv) = eV
        val (le, ce) = e
        if (le == lv && ce == cv) accV +1
        else accV
      })
      assert(nbElement <= 1, "Doublons dans une liste")
      acc + nbElement
    })
  }
  
  def survivantes(g:Grille): Grille = {
    def aux(grille: Grille):Grille = grille match {
      case t::q => {
        val (l, c) = t
        //print("(" + l +"," +c +") :" )
        val nb = nbVivanteParmiVoisines(g,  voisines8(l, c))
        //print(nb + " dans g : " + g.toString() + " \n" )
        if ( 2 <= nb && nb <= 3) t::aux(q)
        else aux(q)
      }
      case Nil => Nil
    }
    aux(g)
  }

  /*
   * Question 5
   */

  /**
   * Si la cellule n'est pas dans g alors true sinon false
   */
  def estMorte(cellule:(Int,Int),g:Grille):Boolean = {
    val (l,c) = cellule
    def aux(grille:Grille):Boolean = grille match {
      case t::q => {
        val (lv,cv) = t
        if(lv==l && cv ==c) false
        else aux(q)
      }

      case Nil => true
    }
    aux(g)

  }

  def candidates(g:Grille):Grille = {
    def aux(grille: Grille):Grille = grille match {
      case t::q => {
        val (l, c) = t
        concatener(voisines8(l,c).filter(cel => estMorte(cel,g)),aux(q))
      }
      case Nil => Nil
    }
    aux(g)
  }

  def concatener(g1:Grille,g2:Grille):Grille = {
    def aux(g1:Grille,g2:Grille):Grille = (g1,g2) match {
      case (Nil,Nil) => Nil
      case (t::q,Nil) => t::aux(q,g2)
      case (Nil,t::q) => t::aux(g1,q)
      case (t1::q1,t2::q2) => (t1,t2) match {
        case ((l1, _), (l2, _)) if l1 < l2 => t1::aux(q1,g2)
        case ((l1, _), (l2, _)) if l1 > l2 => t2::aux(g1,q2)
        case ((_, c1), (_, c2)) if c1 < c2 => t1::aux(q1,g2)
        case ((_, c1), (_, c2)) if c1 > c2 => t2::aux(g1,q2)
        case _ => t1::aux(q1,q2)
      }

    }
    aux(g1,g2)
  }


  /*
   * Question 6
   */
  def retirerDoublons(g:Grille):Grille = g match {
    case t::q => if (q.contains(t)) retirerDoublons(q) else t::retirerDoublons(q)
    case Nil => Nil
  }

  def naissances(g:Grille):Grille = {
    def aux(grille:Grille):Grille = grille match {
      case (l, c)::q => if (nbVivanteParmiVoisines(g,voisines8(l, c)) == 3) (l, c)::aux(q) else aux(q)
      case Nil => Nil
    }
    aux(candidates(g))
  }

  /*
   * Question 7
   */
  def jeuDeLaVieQuestion7(init:Grille, nb:Int):Unit = {
    def aux(i:Int, g :Grille):Unit = {

      val newGrille = concatener(naissances(g),survivantes(g))

      //debuggage a retirer plus tard
      print("------------------- " + i + "/" + nb + "-------------------\n")
      //print("Depart : " + g +"\n")
      //afficherGrille(g)
      //print("Naissance :" + naissances(g) +" \n")
      //afficherGrille(naissances(g))
      //print("Survivantes : " + survivantes(g) +" \n")
      //afficherGrille(survivantes(g))
       print("Total :" + newGrille +" \n")


      afficherGrille(newGrille)
      if (i < nb) aux(i+1, newGrille)
    }
    aux(1, init)
  }

  /*
   * Question 8
   */
  def voisines4(l:Int, c:Int):List[(Int, Int)] = {
    if (estOOB(l,c)) throw new IndexOutOfBoundsException
    List((l-1, c), (l, c-1),(l, c+1), (l+1, c))
  }

  /*
   * Question 9
   */
  def naitJDLV(nbVoisines:Int):Boolean = nbVoisines==3
  def survitJDLV(nbVoisines:Int):Boolean = 2 == nbVoisines || nbVoisines == 3

  def naitFredkin(nbVoisines:Int):Boolean = nbVoisines % 2 != 0
  def survitFredkin(nbVoisines:Int):Boolean = nbVoisines % 2 != 0

  /*
   * Question 10
   */
  def survivantesG(g:Grille, fVoisine:(Int, Int)=>Grille, fSurvit:Int=>Boolean): Grille = {
    def aux(grille: Grille):Grille = grille match {
      case t::q => {
        val (l, c) = t
        if ( fSurvit(nbVivanteParmiVoisines(g,fVoisine(l, c)) ) ) t::aux(q)
        else aux(q)
      }
      case Nil => Nil
    }
    aux(g)
  }

  def candidatesG(g:Grille, fVoisine:(Int, Int)=>Grille):Grille = {
    def aux(grille: Grille):Grille = grille match {
      case t::q => {
        val (l, c) = t
        concatener(fVoisine(l,c).filter(cel => estMorte(cel,g)),aux(q))
      }
      case Nil => Nil
    }
    aux(g)
  }

  def naissancesG(g:Grille, fVoisine:(Int, Int)=>Grille, fNait:Int=>Boolean):Grille = {
    def aux(grille:Grille):Grille = grille match {
      case (l, c)::q => if (fNait( nbVivanteParmiVoisines(g,fVoisine(l, c)) )) (l, c)::aux(q) else aux(q)
      case Nil => Nil
    }
    aux(candidates(g))
  }


  /*
   * Question 11
   */
  def moteur(init:Grille, nb:Int,fVoisine:(Int, Int)=>Grille,  fNait:Int=>Boolean, fSurvit:Int=>Boolean):Unit = {
    @tailrec
    def aux(i:Int, g :Grille):Unit = {

      val newGrille = concatener(naissancesG(g, fVoisine, fNait),survivantesG(g, fVoisine, fSurvit))

      //debuggage a retirer plus tard
      print("------------------- " + i + "/" + nb + "-------------------\n")
      //print("Depart : " + g +"\n")
      //afficherGrille(g)
      //print("Naissance :" + naissances(g) +" \n")
      //afficherGrille(naissances(g))
      //print("Survivantes : " + survivantes(g) +" \n")
      //afficherGrille(survivantes(g))
      print("Total :" + newGrille +" \n")


      afficherGrille(newGrille)
      if (i < nb) aux(i+1, newGrille)
    }
    aux(1, init)
  }

  /*
   * Question  12
   */
  def jeuDeLaVie(init:Grille, nb:Int):Unit = moteur(init, nb, voisines8, naitJDLV, survitJDLV)
  def automateDeFredkin(init:Grille, nb:Int):Unit = moteur(init, nb, voisines4, naitFredkin, survitFredkin)

  /*
   * Question 13
   */
  def varianteAutomateDeFredkin(init:Grille, nb:Int):Unit = moteur(init, nb, (l, c)=>{
    if (estOOB(l,c)) throw new IndexOutOfBoundsException
    List((l-1, c-1), (l-1, c+1),(l+1, c-1), (l+1, c+1))
  }, naitFredkin, survitFredkin)

}
