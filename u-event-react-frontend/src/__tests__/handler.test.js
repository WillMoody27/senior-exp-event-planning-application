import { expect } from "chai";
import sinon from "sinon";
import {
  handleFilterChange,
  handleSearchChange,
  filterData,
} from "..//handlers/handler";

describe("Handler Functions", () => {
  describe("handleFilterChange", () => {
    it("should call setFilter with the new filter value", () => {
      const setFilter = sinon.spy();
      const mockEvent = { target: { value: "new filter" } };

      handleFilterChange(mockEvent, setFilter);

      expect(setFilter.calledOnceWith("new filter")).to.be.true;
    });
  });

  describe("handleSearchChange", () => {
    it("should call setSearchInput and setFilteredData with the correct values", () => {
      const setSearchInput = sinon.spy();
      const setFilteredData = sinon.spy();
      const mockEvent = { target: { value: "test" } };
      const mockData = [
        { eventName: "Test Event" },
        { eventName: "Another Event" },
      ];

      handleSearchChange(mockEvent, mockData, setSearchInput, setFilteredData);

      expect(setSearchInput.calledOnceWith("test")).to.be.true;
      expect(setFilteredData.calledOnceWith([{ eventName: "Test Event" }])).to
        .be.true;
    });
  });

  describe("filterData", () => {
    it("should return filtered data based on the filter", () => {
      const mockData = [
        { category: "concert", eventName: "Concert Event" },
        { category: "sports", eventName: "Sports Event" },
      ];
      const filter = "concert";

      const result = filterData(filter, mockData);

      expect(result).to.deep.equal([
        { category: "concert", eventName: "Concert Event" },
      ]);
    });
  });
});
