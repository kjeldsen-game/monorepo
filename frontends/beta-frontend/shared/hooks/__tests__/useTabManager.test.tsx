import { act, renderHook } from "@testing-library/react";
import { useTabManager } from "../useTabManager";
import React from "react";

describe("useTabManager", () => {

    it("should init with selectedTab set to 0", () => {
        const { result } = renderHook(() => useTabManager());
        expect(result.current.selectedTab).toBe(0);
    })

    it("should update the selected tab", () => {
        const { result } = renderHook(() => useTabManager());
        act(() => {
            result.current.handleTabChange({} as React.SyntheticEvent, 2)
        })

        expect(result.current.selectedTab).toBe(2)
    })
})