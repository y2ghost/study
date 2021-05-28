def binary_search(l, item):
    low = 0;
    high = len(l) - 1

    while low <= high:
        mid = (low + high) // 2
        guess = l[mid]

        if guess == item:
            return mid
        elif guess > item:
            high = mid - 1
        else:
            low = mid + 1

    return None
