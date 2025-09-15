import { act, renderHook } from "@testing-library/react";
import { useModalManager } from "../useModalManager";

describe("useModalManager", () => {

    it("should initialize hook with closed modal", () => {
        const { result } = renderHook(() => useModalManager());
        expect(result.current.open).toBe(false);
    })

    it("should turn modal to open", () => {
        const { result } = renderHook(() => useModalManager());
        act(() => {
            result.current.setOpen(true)
        })

        expect(result.current.open).toBe(true)
    })


    it("should close modal", () => {
        const { result } = renderHook(() => useModalManager());
        result.current.setOpen(false);

        act(() => {
            result.current.handleCloseModal()
        })
        expect(result.current.open).toBe(false)
    })
})