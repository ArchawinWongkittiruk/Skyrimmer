(define (problem p1) (:domain skyraider)

;combined from problems 13, 14, and 17 

(:objects 
    outside entrancehall cloister library observatory - location
    fruitbasket brokenbow brokenaxe - item ;outside
    cookedrat - item ;entrancehall
    warhorn silverlamp - item ;cloister
    dustytome scrawlednote TheLustyLizardMaidVol1 TheLustyLizardMaidVol2 ADreamOfValhalla TheGreatWar - item ;library
    oldscroll weavedbasket tankard - item ;observatory
    bandit1 - npc ;outside
    bandit2 bandit3 - npc ;entrancehall
    bandit4 - npc ;cloister
    banditboss - npc ;observatory
    antechamberkey - item ;on banditboss

    upperstairwell lowerstairwell - location

    antechamber armoury grandhall crypt shrine exit - location
    goldenclaw stonechalice - item ;antechamber
    ancientshield rustyironmace rustyancientsword brokensteeldagger - item ;armoury
    goldnecklace - item ;grandhall
    ancientsword silverring jewelledring - item ;crypt
    incense amulet - item ;shrine
    draugr1 - npc ;antechamber
    giantspider - npc ;armoury
    draugr2 draugr3 draugrboss - npc ;grandhall
    draugr4 - npc ;crypt
    spirit - npc ;shrine
    poison - item ;on giantspider 
    ancienttablet - item ;on draugrboss
    exitkey - item ;on spirit

    riverwood meadery stables citygates marketplace store - location
    soulgem sweetroll carrot - item
    nazeem belethor - npc

    gildergreen dragonsreach - location
    lucan farengar - npc
)

(:init 
    (= (health) 50)
    (= (total-value-score) 0)
    (= (bag-space) 50)

    (= (value cookedrat) 5)
    (= (value tankard) 5)
    (= (value fruitbasket) 10)
    (= (value warhorn) 10)
    (= (value dustytome) 10)
    (= (value weavedbasket) 10)
    (= (value ADreamOfValhalla) 10)
    (= (value brokenbow) 20)
    (= (value brokenaxe) 20)
    (= (value scrawlednote) 20)
    (= (value TheGreatWar) 20)
    (= (value silverlamp) 50)
    (= (value TheLustyLizardMaidVol1) 50)
    (= (value TheLustyLizardMaidVol2) 50)
    (= (value oldscroll) 100)
    (= (value antechamberkey) 100)

    (= (value rustyironmace) 10)
    (= (value rustyancientsword) 10)
    (= (value brokensteeldagger) 10)
    (= (value incense) 10)
    (= (value silverring) 30)
    (= (value amulet) 30)
    (= (value poison) 30)
    (= (value stonechalice) 50)
    (= (value exitkey) 50)
    (= (value ancientshield) 50)
    (= (value goldenclaw) 100)
    (= (value ancientsword) 100)
    (= (value goldnecklace) 100)
    (= (value ancienttablet) 200)
    (= (value jewelledring) 200)

    (= (value soulgem) 30)
    (= (value sweetroll) 10)
    (= (value carrot) 10)

    (= (item-space antechamberkey) 1)
    (= (item-space scrawlednote) 1)
    (= (item-space cookedrat) 3)
    (= (item-space tankard) 3)
    (= (item-space warhorn) 3)
    (= (item-space silverlamp) 3)
    (= (item-space oldscroll) 3)
    (= (item-space dustytome) 4)
    (= (item-space ADreamOfValhalla) 4)
    (= (item-space TheGreatWar) 4)
    (= (item-space TheLustyLizardMaidVol1) 4)
    (= (item-space TheLustyLizardMaidVol2) 4)
    (= (item-space fruitbasket) 7)
    (= (item-space brokenaxe) 7)
    (= (item-space brokenbow) 7)
    (= (item-space weavedbasket) 7)

    (= (item-space poison) 1)
    (= (item-space silverring) 1)
    (= (item-space jewelledring) 1)
    (= (item-space amulet) 1)
    (= (item-space goldnecklace) 1)
    (= (item-space exitkey) 1)
    (= (item-space incense) 2)
    (= (item-space stonechalice) 2)
    (= (item-space ancienttablet) 2)
    (= (item-space brokensteeldagger) 2)
    (= (item-space rustyironmace) 8)
    (= (item-space rustyancientsword) 8)
    (= (item-space ancientsword) 8)
    (= (item-space goldenclaw) 8)
    (= (item-space ancientshield) 10)

    (= (item-space soulgem) 1)
    (= (item-space sweetroll) 1)
    (= (item-space carrot) 1)
    
    (item-at-location fruitbasket outside)
    (item-at-location brokenbow outside)
    (item-at-location brokenaxe outside)
    (item-at-location cookedrat entrancehall)
    (item-at-location warhorn cloister)
    (item-at-location silverlamp cloister)
    (item-at-location dustytome library)
    (item-at-location scrawlednote library)
    (item-at-location TheLustyLizardMaidVol1 library)
    (item-at-location TheLustyLizardMaidVol2 library)
    (item-at-location ADreamOfValhalla library)
    (item-at-location TheGreatWar library)
    (item-at-location oldscroll observatory)
    (item-at-location weavedbasket observatory)
    (item-at-location tankard observatory)

    (item-at-location goldenclaw antechamber)
    (item-at-location stonechalice antechamber)
    (item-at-location ancientshield armoury)
    (item-at-location rustyironmace armoury)
    (item-at-location rustyancientsword armoury)
    (item-at-location brokensteeldagger armoury)
    (item-at-location goldnecklace grandhall)
    (item-at-location ancientsword crypt)
    (item-at-location silverring crypt)
    (item-at-location jewelledring crypt)
    (item-at-location incense shrine)
    (item-at-location amulet shrine)

    (item-at-location carrot marketplace)

    (npc-enemy bandit1)
    (npc-enemy bandit2)
    (npc-enemy bandit3)
    (npc-enemy bandit4)
    (npc-enemy banditboss)

    (npc-enemy draugr1)
    (npc-enemy draugr2)
    (npc-enemy draugr3)
    (npc-enemy draugr4)
    (npc-enemy giantspider)
    (npc-enemy draugrboss)
    (npc-enemy spirit)

    (npc-enemy nazeem)
    (not (npc-enemy belethor))

    (not (npc-enemy lucan))
    (not (npc-enemy farengar))

    (item-on-npc antechamberkey banditboss)

    (item-on-npc poison giantspider)
    (item-on-npc ancienttablet draugrboss)
    (item-on-npc exitkey spirit)

    (item-on-npc soulgem nazeem)
    (item-on-npc sweetroll belethor)

    (npc-at-location bandit1 outside)
    (npc-at-location bandit2 entrancehall)
    (npc-at-location bandit3 entrancehall)
    (npc-at-location bandit4 cloister)
    (npc-at-location banditboss observatory)

    (npc-at-location draugr1 antechamber)
    (npc-at-location giantspider armoury)
    (npc-at-location draugr2 grandhall)
    (npc-at-location draugr3 grandhall)
    (npc-at-location draugrboss grandhall)
    (npc-at-location draugr4 crypt)
    (npc-at-location spirit shrine)

    (npc-at-location nazeem marketplace)
    (npc-at-location belethor store)

    (npc-at-location lucan riverwood)
    (npc-at-location farengar dragonsreach)

    (npc-wants-item belethor soulgem)
    (npc-wants-item belethor carrot)

    (location-has-neighbour outside entrancehall)
    (location-has-neighbour entrancehall cloister)
    (location-has-neighbour cloister library)
    (location-has-neighbour cloister observatory)

    (location-has-neighbour cloister upperstairwell)
    (location-has-neighbour upperstairwell lowerstairwell)
    (location-has-neighbour lowerstairwell antechamber)

    (location-requires-item antechamber antechamberkey)

    (location-has-neighbour antechamber armoury)
    (location-has-neighbour antechamber grandhall)
    (location-has-neighbour grandhall crypt)
    (location-has-neighbour crypt shrine)

    (location-requires-item exit exitkey)

    (location-has-neighbour crypt exit)

    (location-has-neighbour outside riverwood)
    (location-has-neighbour outside meadery)

    (location-has-neighbour exit riverwood)
    (location-has-neighbour exit meadery)
    (location-has-neighbour riverwood meadery)
    (location-has-neighbour meadery stables)
    (location-has-neighbour stables citygates)
    (location-has-neighbour citygates marketplace)
    (location-has-neighbour marketplace store)
    (location-has-neighbour marketplace gildergreen)
    (location-has-neighbour gildergreen dragonsreach)

    (player-at-location outside)
)

(:goal (and
    (item-on-npc goldenclaw lucan)
    (item-on-npc ancienttablet farengar)
    (not (npc-at-location nazeem marketplace))
    (item-in-bag sweetroll)
))

(:metric maximize (total-value-score))

)