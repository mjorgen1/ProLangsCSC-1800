#!/usr/bin/python
import sys

def isParent(name1, name2):
    if not name1 in tree or not name2 in tree:
        return False
    if name1 not in tree.get(name2)[0]:
        return False
    return True

def isSibling(name1, name2):
    if not name1 in tree or not name2 in tree:
        return False

    return set(tree[name1][0]) == set(tree.get(name2)[0])

def ishalfSibling(name1, name2):
    if not name1 in tree or not name2 in tree:
        return False

    if len(set(tree[name1][0]).intersection(tree.get(name2)[0])) == 1:
        return True
    return False


def isAncestorOf(name1, name2):
    if not name1 in tree or not name2 in tree:
        return False
    if len(tree[name2][0]) != 2:
        return False
    name2Parent1 = tree.get(name2)[0][0]
    name2Parent2 = tree.get(name2)[0][1]
    if name2Parent1 == name1 or name2Parent2 == name1:
        return True
    else:
        return isAncestorOf(name1, name2Parent1) or isAncestorOf(name1, name2Parent2)

def isCousinOf(name1, name2):
    if not name1 in tree or not name2 in tree:
        return False
    if (name1 == name2):
        return False
    ancList1 = listAncestor(name1)
    ancList2 = listAncestor(name2)
    for name in ancList1:
        if name in ancList2:
            return True
    return False

def listAncestor(name):
    ancList = []
    for i in listOfNames:
        if isAncestorOf(i, name) and i != name:
            ancList.append(i)
    return ancList

def listSibling(name):
    siblingList = []
    for i in listOfNames:
        if tree.get(name)[0] == tree.get(i)[0] and name != i:
            siblingList.append(i)
    return siblingList

def listCousin(name1):
    if not name1 in tree or not name2 in tree:
        return False
    commonCousins = []
    i=0
    while (i < len(tree)):
        pName = listOfNames[i]
        if not isAncestorOf(pName, name1):
            if isCousinOf(name1, pName) == True:
                commonCousins.append(pName)
        i +=1
    return commonCousins

if __name__ == "__main__":
    tree = dict()
    listOfNames = []


    for line in sys.stdin:
        if line.startswith('E'):
            y = line.split()
            for i in range(1,len(y)):
                persons = []    # key
                parents = []    #0th index
                persons.append(parents)
                if y[i] not in listOfNames:
                    listOfNames.append(y[i])
                    if i == 1:
                        tree.update({y[i]: persons})
                    if i == 2:
                        tree.update({y[i]: persons})
                    try:
                        if i == 3:
                            tree.update({y[i]: persons})
                            parents.append(y[1])
                            parents.append(y[2])
                    except IndexError as e:
                        pass
                else:
                    try:
                        if i == 3:
                            if y[1] not in tree.get(y[i])[0]:
                                tree.get(y[i])[0].append(y[1])
                            if y[2] not in tree[y[i]][0]:
                                tree.get(y[i])[0].append(y[2])
                    except IndexError as e:
                        pass
        elif line.startswith('X'):
            relation = line.split()[2]
            name1 = line.split()[1]
            name2 = line.split()[3]
            print("X {} {} {}".format(name1, relation, name2))

            if relation == 'parent':
                result = isParent(name1, name2)
            elif relation == 'sibling':
                result = isSibling(name1,name2)
            elif relation == 'half-sibling':
                result = ishalfSibling(name1, name2)
            elif relation == 'ancestor':
                result = isAncestorOf(name1, name2)
            elif relation == 'cousin':
                result = isCousinOf(name1, name2)
            elif relation == 'unrelated':
                if not isCousinOf(name1, name2) and name1 != name2:
                    if not isAncestorOf(name1, name2) and not isAncestorOf(name2, name1) :
                        result = True
                else:
                    result = False
            if result :
                print("Yes")
            else:
                print("No")
            print
        elif line.startswith('W'):
            line = line.split()
            relation = line[1]
            name = line[2]
            printList = []

            print("W {} {}".format(relation, name))
            if name in tree:
                if relation == 'parent' :
                    printList = tree.get(name)[0]
                elif relation == 'sibling' :
                    printList=listSibling(name)
                elif relation == 'half-sibling':
                    halfSibs = []
                    for i in listOfNames:
                        if ishalfSibling(name, i):
                            halfSibs.append(i)
                    printList = halfSibs
                elif relation == 'ancestor' :
                    ancList=listAncestor(name)
                    printList = ancList
                elif relation == 'cousin' :
                    cousList = listCousin(name)
                    printList = cousList
                elif relation == 'unrelated' :
                    unrelList = []
                    for i in listOfNames:
                        if not isCousinOf(i, name) and i != name:
                            if not isAncestorOf(i, name) and not isAncestorOf(name, i):
                                unrelList.append(i)
                        printList = unrelList
                printList.sort()
                for i in printList:
                    print(i)
                print("")