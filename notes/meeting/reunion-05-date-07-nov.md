# Réunion 5

**Date et heure.** 7 novembre 2018 à 14h30

**Prochaine réunion.** 14 novembre 2018 à 14h30

**Ordre du jour.**

1. Jalons
2. Assignement de Tristan
3. Travail à faire

## 1. Jalons

On va commencer par le travail de chacun. Les règles de l'arbre abstraits ont été testées par Alexis, mais pas toutes.

Les règles supplémentaires ont été pushées par David. Il a également ajouté des tests, chacun très atomique. Il a ajouté le PDF de Antlr dans les docs.

Penser à compiler avant de lancer le test avec `make build`.

Philippe remarque que certains tests fail semblent pourtant correct. Tristan rappelle qu'un fichier tiger ne peut pas contenir seulement un commentaire. David demande à Tristan d'expliquer sa nomenclature des fichiers tests pour les commentaires.

Tristan pense qu'Antlr ne provoque pas immédiatement d'erreurs concernants les réécritures, mais en provoque lors des tests.

Philippe a écrit deux tests et en réécrira la semaine prochaine.

Philippe met quelques ressources sur la table des symboles sur le drive.

David se demande à quelle point il faut avancer les tests. Tristan pense qu'il ne faut faire que les tests. 

Tristan évoque ses tests. Il en a ajouté pas mal qui testent surtout les opérations arithmétiques. Les affectations font aussi partie des opérations "arithmétiques". Suite à l'ajout des affectation, il y a plus de tests qui passent. Normalement, à son prochain commit, tous ses tests devraient passer, même s'il y a d'autres problèmes.

## 2. Assignement de Tristan

D'autres points étaient en suspens, notamment les affectations, TYID et ID posant problème. Les affectations sont détaillées sur gitlab dans un fichier ISSUES.md. Il explique pourquoi on avait un problème et comment il a été "résolu". Une affectation s'écrit lValue ':=' exp. Cela pose problème car quand on lit une expression, elle peut commencer par un lValue. Ce lValue, on ne sait pas s'il est en plein milieu d'une expression arithmétique ou s'il est associé à une affectation. Quand on analyse l'expression globale, on ne sait pas s'il va s'agir d'une affectation ou d'une multiplication, il faut lire des caractères en avance.

Ce que l'on va faire, c'est considérer qu'il est valide d'écrire (a*b):=c d'un point de vu *syntaxique*, mais que sa *sémantique* est fausse.

## 3. Travail à faire

**Alexis.**

- Ajouter des tests plus complexes
- Tester les réécritures

**Philippe.**

- Ajouter des tests plus complexes
- Effectuer au moins deux tests par règles

**David.**

- Ajouter des tests plus complexes

**Tristan.**

- Ajouter des tests plus complexes

