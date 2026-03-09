def findSmallest(arr):
    smallest = arr[0]
    idx = 0

    for i in range(1, len(arr)):
        if arr[i] < smallest:
            smallest = arr[i]
            idx = i

    return idx

def select_sort(arr):
    newArr = []
    for i in range(len(arr)):
        smallest = findSmallest(arr)
        newArr.append(arr.pop(smallest))

    return newArr
