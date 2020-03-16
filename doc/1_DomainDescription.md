# Bounded context: Order-Taking

## Data types
```
data WidgetCode = string starting with "W" then 4 digits
data GizmoCode = string starting wiht "G" then 3 digits
data ProductCode = WidgetCode OR GizmoCode
---
data UnitQuantity = integer between 1 and 1000
data KilogramQuantity = decimal between 0.05 and 100.00
data OrderQuantity = UnitQuantity OR KilogramQuantity
---
data UnvalidatedOrder = 
    UnvalidatedCustomerInfo
    AND UnvalidatedShippingAddress
    AND UnvalidatedBillingAddress
    AND list of UnvalidatedOrderLine

data UnvalidatedOrderLine =
    UnvalidatedProductCode 
    AND UnvalidatedOrderQuantity
---
data ValidatedOrder = 
    ValidatedCustomerInfo
    AND ValidatedShippingAddress
    AND ValidatedBillingAddress
    AND list of ValidatedOrderLine

data ValidatedOrderLine =
    ValidatedProductCode 
    AND ValidatedOrderQuantity
---
data PricedOrder =
    ValidatedCustomerInfo
    AND ValidatedShippingAddress
    AND ValidatedBillingAddress
    AND list of PricedOrderLine
    AND AmountToBill

data PricedOrderLine =
    ValidatedOrderLine
    AND LinePrice
---
data PlacedOrderAcknowglement = 
    PricedOrder
    AND AcknowglementLetter
```

## Workflow (describe process and substeps)
```
Workflow "Place order" =
    input: OrderForm
    output:
        OrderPlacedEvent
        OR InvalidOrder

    // step 1
    do ValidateOrder
    If oders is invalid then:
        add Invalid order to pile
        stop
    
    // step 2
    do PriceOrder

    // step 3 
    do SentAcknowglementToCustomer

    // step 4
    return OrderPlaced event
---
substep "ValidateOrder" = 
    input: UnvalidatedOrder
    output: ValidatedOrder OR ValidationError
    dependencies: CheckProductCodeExists, CheckAddressExists

    validate customerName
    check that the shopping and billing address exist
    for each line:
        check product code syntax
        check that product code exists in ProductCatalog

    if everythingOK then:
        return ValidatedOrder
    else:
        return ValidationError
--- 
substep "PriceOrder" =
    input: ValidatedOrder
    output: PricedOrder
    dependencies: GetProductPrice

    for each line:
        get the price for the product
        set the price for the line
    set the amount to bill
---
substep "SentAcknowglementToCustomer" = 
    input: PricedOrder
    output: None

    create acknowglement letter and send it
    and the priced order to the customer
```