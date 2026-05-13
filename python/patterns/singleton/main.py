from copy import copy
from executive_pass import ExecutivePass
  

def run_app():
    ron_pass = ExecutivePass.obtain("Ron")
    print("Ron takes hold of the executive pass")
    print(f"  Ron's pass: {ron_pass}")
    print()

    sal_pass = ExecutivePass.obtain("Sal")
    print("Sal takes the executive pass from Ron")
    print(f"  Sal's pass: {sal_pass}")
    print()

    sal_pass = ExecutivePass.delete()

    pat_pass = ExecutivePass.obtain("Pat")
    print("Successfully created a new executive pass for Pat")
    print(f"  Pat's pass: {pat_pass}")
    print()

    leo_pass = pat_pass
    print("Pat's pass is assigned to Leo")
    print("Now Pat and Leo together hold Pat's pass")
    print(f"  Leo's pass: {leo_pass}")
    print(f"  Pat's pass: {leo_pass}")
    
    print()   
    print("Try to explicitly create another executive pass")
    tim_pass = ExecutivePass()
    
    print()
    print("Try to make a copy of Pat's pass")
    bob_pass = copy(pat_pass)

if __name__ == '__main__':    
    run_app()
    
