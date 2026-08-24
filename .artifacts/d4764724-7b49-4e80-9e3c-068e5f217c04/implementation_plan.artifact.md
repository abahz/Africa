# Implementation Plan - CRUD for All Remaining Models

This plan outlines the creation of Repositories and ViewModels for the remaining models: `Category`, `Orders`, `OrderItem`, `Carts`, `CartItem`, and `Customer`.

## User Review Required

> [!IMPORTANT]
> I will add an `id: String = ""` field to `OrderItem` and `Customer` to allow CRUD operations in Supabase, consistent with the other models.

## Proposed Changes

### Model Updates

#### [MODIFY] [Model.kt](file:///C:/ABAHZ/Africa2/shared/src/commonMain/kotlin/com/abahz/africa/model/Model.kt)
- Add `id: String = ""` to `OrderItem` and `Customer`.

### Repositories

I will create the following repositories in `com.abahz.africa.repository`:
- `CategoryRepository`
- `OrderRepository` (Handles `Orders` and `OrderItem`)
- `CartRepository` (Handles `Carts` and `CartItem`)
- `CustomerRepository`

### ViewModels

I will create the following ViewModels in `com.abahz.africa.viewmodel`:
- `CategoryViewModel`
- `OrderViewModel`
- `CartViewModel`
- `CustomerViewModel`

### Dependency Injection

#### [MODIFY] [SupabaseModule.kt](file:///C:/ABAHZ/Africa2/shared/src/commonMain/kotlin/com/abahz/africa/di/SupabaseModule.kt)
- Register all new Repositories and ViewModels in the Koin module.

## Verification Plan

### Manual Verification
- Ensure the project builds successfully.
- Verify Koin registration for all new components.
